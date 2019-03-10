package ubb.ntt.bookstore.Repository.xml_repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ubb.ntt.bookstore.Domain.Sell;
import ubb.ntt.bookstore.Domain.validators.Validator;
import ubb.ntt.bookstore.Domain.validators.ValidatorException;
import ubb.ntt.bookstore.Repository.in_memory.InMemoryRepository;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SellRepository extends InMemoryRepository<Long, Sell> {

    private static String fileName;

    public SellRepository(Validator<Sell> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadSells();
    }

    private static void saveDataToXML(Sell sell) {
        try {
            Document document =
                    DocumentBuilderFactory.newInstance()
                            .newDocumentBuilder()
                            .parse(fileName);
            Element root = document.getDocumentElement();
            root.appendChild(createSellElement(document, sell));

            Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document),
                    new StreamResult(new File(fileName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Node createSellElement(Document document, Sell sell) {
        Element sellElement = document.createElement("sell");
        sellElement.setAttribute("id", String.valueOf(sell.getId()));

        appendTagWithText(document, sellElement, "Id",
                String.valueOf(sell.getId()));
        appendTagWithText(document, sellElement, "book_id",
                String.valueOf(sell.getBookId()));
        appendTagWithText(document, sellElement, "client_id",
                String.valueOf(sell.getClientId()));
        appendTagWithText(document, sellElement, "price",
                String.valueOf(sell.getPrice()));
        appendTagWithText(document, sellElement, "date",
                String.valueOf(sell.getDate()));

        return sellElement;
    }

    private static void appendTagWithText(Document document, Element parent,
                                          String tagName, String textContent) {
        Element element = document.createElement(tagName);
        element.setTextContent(textContent);
        parent.appendChild(element);
    }

    private static Sell createSellFromElement(Element sellElement) {
        Sell sell = new Sell();

        Long id1 = Long.valueOf(sellElement.getAttribute("id"));
        sell.setId(id1);

        Long id = Long.valueOf(getTextFromTag(sellElement, "Id"));
        sell.setId(id);

        Long book_id = Long.valueOf(getTextFromTag(sellElement, "book_id"));
        sell.setBookId(book_id);

        Long client_id = Long.valueOf(getTextFromTag(sellElement, "client_id"));
        sell.setClientId(client_id);

        Long price = Long.valueOf(getTextFromTag(sellElement, "price"));
        sell.setPrice(price);

        String date = getTextFromTag(sellElement, "date");
        sell.setDate(date);


        return sell;
    }

    private static String getTextFromTag(Element sellElement, String tagName) {
        NodeList children = sellElement.getElementsByTagName(tagName);
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                Element element = (Element) children.item(i);
                return element.getTextContent();
            }
        }
        return null;
    }

    @Override
    public Optional<Sell> save(Sell entity) throws ValidatorException {
        Optional<Sell> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveDataToXML(entity);
        return Optional.empty();
    }

    @Override
    public Optional<Sell> delete(Long id) {
        Optional<Sell> optional = super.delete(id);
        if (id == null||!super.entities.containsKey(id)) {
            throw new ValidatorException("there's no sell with the id "+id+" in the list.\nPlease enter a valid id!\n");
        }
        updateXML();
        return Optional.empty();
    }

    @Override
    public Optional<Sell> update(Sell sell) throws ValidatorException{
        Optional<Sell> optional = super.update(sell);
        if (sell.getId() == null || !super.entities.containsKey(sell.getId())) {
            throw new ValidatorException("sells list doesn't contain id: " + sell.getId() + "\nPlease enter a valid id!\n");
        }
        updateXML();
        return Optional.empty();
    }

    private void updateXML() {

        try {
            Document document =
                    DocumentBuilderFactory.newInstance()
                            .newDocumentBuilder()
                            .newDocument();

            Element root = document.createElement("sells");
            document.appendChild(root);

            findAll().forEach(s -> {
                Element sell = document.createElement("sell");
                root.appendChild(sell);

                sell.setAttribute("id", String.valueOf(s.getId()));

                Element Id = document.createElement("Id");
                Id.appendChild(document.createTextNode(String.valueOf(s.getId())));
                sell.appendChild(Id);

                Element book_id = document.createElement("book_id");
                book_id.appendChild(document.createTextNode(String.valueOf(s.getBookId())));
                sell.appendChild(book_id);

                Element client_id = document.createElement("client_id");
                client_id.appendChild(document.createTextNode(String.valueOf(s.getBookId())));
                sell.appendChild(client_id);

                Element price = document.createElement("price");
                price.appendChild(document.createTextNode(String.valueOf(s.getPrice())));
                sell.appendChild(price);

                Element date = document.createElement("date");
                price.appendChild(document.createTextNode(s.getDate()));
                sell.appendChild(date);

            });

            Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document),
                    new StreamResult(new File("./data/sells.xml")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSells() {
        List<Sell> sellings = new ArrayList<>();
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(fileName);
            Element root = document.getDocumentElement();
            NodeList childNodes = root.getChildNodes();

            for (int i = 0; i < childNodes.getLength(); i++) {
                Node sellNode = childNodes.item(i);
                if (sellNode instanceof Element) {
                    Sell sell = createSellFromElement((Element) sellNode);
                    sellings.add(sell);
                    super.save(sell);

                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.getMessage();
        } catch (ValidatorException e) {
            System.out.println("Error while reading sells from xml !!! ");
            System.out.println(e.getMessage());
            System.exit(1);
        }



    }
}
