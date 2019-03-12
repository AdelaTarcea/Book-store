package ubb.ntt.bookstore.Repository.xml_repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ubb.ntt.bookstore.Domain.Customer;
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
/**
 * CustomerRepository class for CRUD operation on  xml repository for a customer object .
 *
 * @author Adela
 */
public class CustomerRepository extends InMemoryRepository<Long, Customer> {

    private static String fileName;

    public CustomerRepository(Validator<Customer> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadClients();
    }

    private static void saveDataToXML(Customer customer) {
        try {
            Document document =
                    DocumentBuilderFactory.newInstance()
                            .newDocumentBuilder()
                            .parse(fileName);
            Element root = document.getDocumentElement();
            root.appendChild(createClientElement(document, customer));

            Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document),
                    new StreamResult(new File(fileName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Node createClientElement(Document document, Customer customer) {
        Element clientElement = document.createElement("customer");
        clientElement.setAttribute("name", customer.getName());

        appendTagWithText(document, clientElement, "Id",
                String.valueOf(customer.getId()));

        appendTagWithText(document, clientElement, "name",
                customer.getName());

        appendTagWithText(document, clientElement, "telephone",
                String.valueOf(customer.getTelephoneNumber()));

        return clientElement;
    }

    private static void appendTagWithText(Document document, Element parent,
                                          String tagName, String textContent) {
        Element element = document.createElement(tagName);
        element.setTextContent(textContent);
        parent.appendChild(element);
    }

    private static Customer createClientFromElement(Element clientElement) {
        Customer customer = new Customer();

        String n = clientElement.getAttribute("name");
        customer.setName(n);

        Long id = Long.valueOf(getTextFromTag(clientElement, "Id"));
        customer.setId(id);

        String name = getTextFromTag(clientElement, "name");
        customer.setName(name);

        String telephoneNumber = getTextFromTag(clientElement, "telephone");
        customer.setTelephoneNumber(telephoneNumber);


        return customer;
    }

    private static String getTextFromTag(Element bookElement, String tagName) {
        NodeList children = bookElement.getElementsByTagName(tagName);
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                Element element = (Element) children.item(i);
                return element.getTextContent();
            }
        }
        return null;
    }

    @Override
    public Optional<Customer> save(Customer entity) throws ValidatorException {
        Optional<Customer> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveDataToXML(entity);
        return Optional.empty();
    }

    @Override
    public Optional<Customer> delete(Long id) throws ValidatorException {
        if (id == null ) {
            throw new IllegalArgumentException(" id can't be null !!");
        }else if ( !super.entities.containsKey(id)){
            throw new ValidatorException("client list doesn't contain id: " + id + "\nPlease enter a valid id!\n");

        }
        Optional<Customer> optional = super.delete(id);
        if (optional.isPresent()) {
            updateXML();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Customer> update(Customer customer) throws ValidatorException {
        if (customer.getId() == null) {
            throw new IllegalArgumentException("id can't be null !!!");
        } else if (!super.entities.containsKey(customer)) {
            throw new ValidatorException("customer list doesn't contain id: " + customer + "\nPlease enter a valid id!\n");
        }
        Optional<Customer> optional = super.update(customer);
        if (optional.isPresent()) {
            updateXML();
        }
        return optional;

    }

    private void updateXML() {

        try {
            Document document =
                    DocumentBuilderFactory.newInstance()
                            .newDocumentBuilder()
                            .newDocument();

            Element root = document.createElement("clientstore");
            document.appendChild(root);


            findAll().forEach(c -> {
                Element client = document.createElement("client");
                root.appendChild(client);

                client.setAttribute("name", c.getName());

                Element Id = document.createElement("Id");
                Id.appendChild(document.createTextNode(String.valueOf(c.getId())));
                client.appendChild(Id);

                Element name = document.createElement("name");
                name.appendChild(document.createTextNode(String.valueOf(c.getName())));
                client.appendChild(name);

                Element telephone = document.createElement("telephone");
                Id.appendChild(document.createTextNode(String.valueOf(c.getId())));
                client.appendChild(telephone);
            });

            Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document),
                    new StreamResult(new File("./data/clientstore.xml")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Customer> loadClients() {
        List<Customer> customers = new ArrayList<>();
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(fileName);
            Element root = document.getDocumentElement();
            NodeList childNodes = root.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node clientNode = childNodes.item(i);
                if (clientNode instanceof Element) {
                    Customer customer = createClientFromElement((Element) clientNode);
                    customers.add(customer);
                    super.save(customer);

                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.getMessage();

        } catch (ValidatorException e) {
            System.out.println("Error while reading customers from xml !!! ");
            System.out.println(e.getMessage());
            System.exit(1);
        }

        return customers;

    }
}
