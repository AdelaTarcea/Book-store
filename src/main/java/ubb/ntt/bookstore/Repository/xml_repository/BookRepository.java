package ubb.ntt.bookstore.Repository.xml_repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ubb.ntt.bookstore.Domain.Book;
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
 * BookRepository class for CRUD operation on  xml repository for a book object .
 *
 * @author Adela
 */
public class BookRepository extends InMemoryRepository<Long, Book> {

    private static String fileName;

    public BookRepository(Validator<Book> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadBooks();
    }

    private static void saveDataToXML(Book book) {
        try {
            Document document =
                    DocumentBuilderFactory.newInstance()
                            .newDocumentBuilder()
                            .parse(fileName);
            Element root = document.getDocumentElement();
            root.appendChild(createBookElement(document, book));

            Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document),
                    new StreamResult(new File(fileName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Node createBookElement(Document document, Book book) {
        Element bookElement = document.createElement("book");
        bookElement.setAttribute("id", String.valueOf(book.getId()));

        appendTagWithText(document, bookElement, "Id",
                String.valueOf(book.getId()));
        appendTagWithText(document, bookElement, "name",
                book.getName());
        appendTagWithText(document, bookElement, "author",
                book.getAuthor());
        appendTagWithText(document, bookElement, "year",
                String.valueOf(book.getYear()));
        appendTagWithText(document, bookElement, "type",
                String.valueOf(book.getType()));

        return bookElement;
    }

    private static void appendTagWithText(Document document, Element parent,
                                          String tagName, String textContent) {
        Element element = document.createElement(tagName);
        element.setTextContent(textContent);
        parent.appendChild(element);
    }

    private static Book createBookFromElement(Element bookElement) {
        Book book = new Book();

        String bookId = bookElement.getAttribute("id");
        book.setId(Long.valueOf(bookId));

        Long id = Long.valueOf(getTextFromTag(bookElement, "Id"));
        book.setId(id);

        String title = getTextFromTag(bookElement, "name");
        book.setName(title);

        String author = getTextFromTag(bookElement, "author");
        book.setAuthor(author);

        int year = Integer.parseInt(getTextFromTag(bookElement, "year"));
        book.setYear(year);

        String type = getTextFromTag(bookElement, "type");
        book.setType(type);


        return book;
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
    public Optional<Book> save(Book entity) throws ValidatorException {
        Optional<Book> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveDataToXML(entity);
        return Optional.empty();
    }

    @Override
    public Optional<Book> delete(Long id) throws ValidatorException {

        if (id == null) {
            throw new IllegalArgumentException(" id can't be null !!");
        } else if (!super.entities.containsKey(id)) {
            throw new ValidatorException("books list doesn't contain id: " + id + "\nPlease enter a valid id!\n");

        }
        Optional<Book> optional = super.delete(id);
        if (optional.isPresent()) {
            updateXML();
        }

        return Optional.empty();
    }

    private void updateXML() {

        try {
            Document document =
                    DocumentBuilderFactory.newInstance()
                            .newDocumentBuilder()
                            .newDocument();

            Element root = document.createElement("bookstore");
            document.appendChild(root);

            findAll().forEach(b -> {
                Element book = document.createElement("book");
                root.appendChild(book);

                book.setAttribute("id", String.valueOf(b.getId()));

                Element Id = document.createElement("Id");
                Id.appendChild(document.createTextNode(String.valueOf(b.getId())));
                book.appendChild(Id);

                Element name = document.createElement("name");
                name.appendChild(document.createTextNode(String.valueOf(b.getName())));
                book.appendChild(name);

                Element author = document.createElement("author");
                author.appendChild(document.createTextNode(String.valueOf(b.getAuthor())));
                book.appendChild(author);

                Element year = document.createElement("year");
                year.appendChild(document.createTextNode(String.valueOf(b.getYear())));
                book.appendChild(year);

                Element type = document.createElement("type");
                author.appendChild(document.createTextNode(String.valueOf(b.getAuthor())));
                book.appendChild(type);

            });

            Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document),
                    new StreamResult(new File("./data/bookstore.xml")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Book> update(Book book) throws ValidatorException {
        Optional<Book> optional = super.update(book);
        if (book.getId() == null || !super.entities.containsKey(book.getId())) {
            throw new ValidatorException("books list doesn't contain id: " + book.getId() + "\nPlease enter a valid id!\n");
        }
        updateXML();
        return Optional.empty();
    }

    private List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(fileName);
            Element root = document.getDocumentElement();
            NodeList bookNodes = root.getChildNodes();

            for (int i = 0; i < bookNodes.getLength(); i++) {
                Node bookNode = bookNodes.item(i);
                if (bookNode instanceof Element) {
                    Book book = createBookFromElement((Element) bookNode);
                    books.add(book);
                    super.save(book);

                }
            }
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.getMessage();
        } catch (ValidatorException e) {
            System.out.println("Error while reading books from xml !!! ");
            System.out.println(e.getMessage());
            System.exit(1);
        }

        return books;

    }
}
