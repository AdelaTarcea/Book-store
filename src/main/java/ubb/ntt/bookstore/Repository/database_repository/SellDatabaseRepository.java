package ubb.ntt.bookstore.Repository.database_repository;

import ubb.ntt.bookstore.Domain.Sell;
import ubb.ntt.bookstore.Domain.validators.Validator;
import ubb.ntt.bookstore.Domain.validators.ValidatorException;
import ubb.ntt.bookstore.Repository.in_memory.InMemoryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SellDatabaseRepository class for CRUD operation on a database repository for a sell object .
 *
 * @author Adela
 */
public class SellDatabaseRepository extends InMemoryRepository<Long, Sell> {

    private static final String URL = "jdbc:postgresql://localhost:5432/book_store";
    private static final String USERNAME = System.getProperty("user");
    private static final String PASSWORD = System.getProperty("password");


    public SellDatabaseRepository(Validator<Sell> validator) {
        super(validator);
        loadData();

    }

    @Override
    public Optional<Sell> save(Sell entity) throws ValidatorException {
        Optional<Sell> optional = super.save(entity);

        if (optional.isPresent()) {
            return optional;
        }
        saveToDataBase(entity);
        return Optional.empty();
    }

    private void saveToDataBase(Sell sell) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME,
                PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "insert into sell( bookid, clientid, date,price ) values (?,?,?,?)")) {


            statement.setLong(1, sell.getBookId());
            statement.setLong(2, sell.getClientId());
            statement.setString(3, sell.getDate());
            statement.setDouble(4, sell.getPrice());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Sell> delete(Long id) {
        Optional<Sell> optional = super.delete(id);
        if (id == null || !super.entities.containsKey(id)) {
            throw new ValidatorException("there's no sell with the id " + id + " in the list.\nPlease enter a valid id!\n");
        }else if(optional.isPresent()){
        deleteDataFromDataBase(id);
        return Optional.empty();
        }
        return optional;
    }

    private void deleteDataFromDataBase(Long id) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME,
                PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "delete from sell where id = ?")
        ) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Sell> update(Sell sell) throws ValidatorException {
        Optional<Sell> optional = super.update(sell);
        if (sell.getId() == null || !super.entities.containsKey(sell.getId())) {
            throw new ValidatorException("sells list doesn't contain id: " + sell.getId() + "\nPlease enter a valid id!\n");
        }
        updateDataInDatabase(sell);
        return Optional.empty();
    }

    private void updateDataInDatabase(Sell sell) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME,
                PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "update sell set bookid = ?,clientid=? ,date=?, price=? where id= ?")) {

            statement.setLong(1, sell.getBookId());
            statement.setLong(2, sell.getClientId());
            statement.setString(3, sell.getDate());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadData() {
        List<Sell> sells = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME,
                PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "select * from sell");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long bookId = resultSet.getLong("bookid");
                Long clientId = resultSet.getLong("clientid");
                String date = resultSet.getString("date");
                Double price = resultSet.getDouble("price");


                Sell sell = new Sell();
                sell.setId(id);
                sell.setBookId(bookId);
                sell.setClientId(clientId);
                sell.setDate(date);
                sell.setPrice(price);

                sells.add(sell);
                super.save(sell);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ValidatorException e) {
            System.out.println("Error while reading sells from database !!! ");
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }
}
