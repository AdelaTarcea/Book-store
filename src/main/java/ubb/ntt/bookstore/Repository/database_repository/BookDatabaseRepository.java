package ubb.ntt.bookstore.Repository.database_repository;

import ubb.ntt.bookstore.Domain.Book;
import ubb.ntt.bookstore.Domain.validators.Validator;
import ubb.ntt.bookstore.Domain.validators.ValidatorException;
import ubb.ntt.bookstore.Repository.in_memory.InMemoryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * BookDatabaseRepository class for CRUD operation on a database repository for a book object .
 *
 * @author Adela
 */
public class BookDatabaseRepository extends InMemoryRepository<Long, Book> {

    private static final String URL = "jdbc:postgresql://localhost:5432/book_store";
    private static final String USERNAME = System.getProperty("user");
    private static final String PASSWORD = System.getProperty("password");


    public BookDatabaseRepository(Validator<Book> validator) {
        super(validator);
        loadData();
    }

    @Override
    public Optional<Book> save(Book entity) {

        Optional<Book> optional = super.save(entity);

        if (!optional.isPresent()) {
            saveToDataBase(entity);
            return Optional.empty();
        }
        return optional;
    }

    private void saveToDataBase(Book book) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

             PreparedStatement statement = connection.prepareStatement(
                     "insert into book( name,author,year,type) values (?,?,?,?)")) {


            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            statement.setLong(3, book.getYear());
            statement.setString(4, book.getType());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.getLocalizedMessage();
        }
    }

    @Override
    public Optional<Book> delete(Long id) throws ValidatorException {
        Optional<Book> optional = super.delete(id);
        if (id == null) {
            throw new IllegalArgumentException(" id must not be null !!");
        } else if (!super.entities.containsKey(id)) {
            throw new ValidatorException("books list doesn't contain id: " + id + "\nPlease enter a valid id!\n");
        }

        if (optional.isPresent()) {
            deleteDataFromDataBase(id);
            return optional;
        }

        return Optional.empty();
    }

    private void deleteDataFromDataBase(Long id) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "delete from book where id = ?")
        ) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    @Override
    public Optional<Book> update(Book book) throws ValidatorException {
        Optional<Book> optional = super.update(book);
        if (book.getId() == null) {
            throw new IllegalArgumentException("id must not be null !!!");
        } else if (!super.entities.containsKey(book.getId())) {
            throw new ValidatorException("books list doesn't contain id: " + book.getId() + "\nPlease enter a valid id!\n");
        }

        if (optional.isPresent()) {
            updateDataInFile(book);
            return Optional.empty();
        }

        return optional;

    }

    private void updateDataInFile(Book book) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME,
                PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "update book set name=?,author=?,year=?,type=?   where id= ?")) {


            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            statement.setString(3, String.valueOf(book.getYear()));
            statement.setString(4, book.getType());
            statement.setLong(5, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
    }


    private void loadData() {
        List<Book> books = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME,
                PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "select * from book");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                int year = Integer.parseInt(resultSet.getString("year"));
                String type = resultSet.getString("type");

                Book book = new Book();
                book.setId(id);
                book.setName(name);
                book.setAuthor(author);
                book.setYear(year);
                book.setType(type);


                books.add(book);
                super.save(book);
            }

        } catch (SQLException e) {
            e.getMessage();

        } catch (ValidatorException e) {
            System.out.println("Error while reading books from database !!! ");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
