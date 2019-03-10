package ubb.ntt.bookstore.Repository.database_repository;


import ubb.ntt.bookstore.Domain.Customer;
import ubb.ntt.bookstore.Domain.validators.Validator;
import ubb.ntt.bookstore.Domain.validators.ValidatorException;
import ubb.ntt.bookstore.Repository.in_memory.InMemoryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CustomerDatabaseRepository class for CRUD operation on a database repository for a costumer object .
 *
 * @author Adela
 */
public class CustomerDatabaseRepository extends InMemoryRepository<Long, Customer> {

    private static final String URL = "jdbc:postgresql://localhost:5432/book_store";
    private static final String USERNAME = System.getProperty("user");
    private static final String PASSWORD = System.getProperty("password");


    public CustomerDatabaseRepository(Validator<Customer> validator) {
        super(validator);
        loadData();

    }

    @Override
    public Optional<Customer> save(Customer entity) throws ValidatorException {
        Optional<Customer> optional = super.save(entity);

        if (!optional.isPresent()) {
            saveToDataBase(entity);
            return Optional.empty();
        }
       return optional;
    }

    private void saveToDataBase(Customer customer) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME,
                PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "insert into client(name,telephone) values (?,?)")) {


            statement.setString(1, customer.getName());
            statement.setString(2, customer.getTelephoneNumber());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Customer> delete(Long id) throws ValidatorException {
        Optional<Customer> optional = super.delete(id);
        if (id == null || !super.entities.containsKey(id)) {
            throw new ValidatorException("there's no client with the id " + id + " in the list.\nPlease enter a valid id!\n");
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
                     "delete from client where id = ?")
        ) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Customer> update(Customer customer) throws ValidatorException {
        Optional<Customer> optional = super.update(customer);
        if(customer.getId() == null ){
            throw new ValidatorException( "id must not be null");
        }
        if ( !super.entities.containsKey(customer.getId())) {
            throw new ValidatorException("clients list doesn't contain id: " + customer.getId() + "\nPlease enter a valid id!\n");
        }
        if (optional.isPresent()) {
            updateDataInFile(customer);
            Optional.empty();
        }
        return optional;
    }

    private void updateDataInFile(Customer customer) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME,
                PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "update client set name=?,telephone=?  where id= ?")) {


            statement.setString(1, customer.getName());
            statement.setString(2, String.valueOf(customer.getTelephoneNumber()));
            statement.setLong(3, customer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadData() {
        List<Customer> customers = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME,
                PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "select * from client");
             ResultSet resultSet = statement.executeQuery()) {


            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String telephone = resultSet.getString("telephone");

                Customer customer = new Customer();
                customer.setName(name);
                customer.setId(id);
                customer.setTelephoneNumber(telephone);


                customers.add(customer);
                super.save(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ValidatorException e) {
            System.out.println("Error while reading customers from database !!! ");
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }


}
