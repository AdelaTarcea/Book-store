package ubb.ntt.bookstore.Repository.file_repository;

import ubb.ntt.bookstore.Domain.Customer;
import ubb.ntt.bookstore.Domain.validators.Validator;
import ubb.ntt.bookstore.Domain.validators.ValidatorException;
import ubb.ntt.bookstore.Repository.in_memory.InMemoryRepository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 * CustomerFileRepository class for CRUD operation on a file repository for a customer object .
 *
 * @author Adela
 */
public class CustomerFileRepository extends InMemoryRepository<Long, Customer> {
    private String fileName;

    public CustomerFileRepository(Validator<Customer> validator, String fileName) {
        super(validator);
        this.fileName = fileName;

        loadData();
    }

    private void loadData() {
        Path path = Paths.get(fileName);

        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(","));

                Long id = Long.valueOf(items.get(0));
                String name = items.get((1));
                String telephoneNumber = items.get(2);

                Customer customer = new Customer(name, telephoneNumber);
                customer.setId(id);

                super.save(customer);
            });

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ValidatorException e) {
            System.out.println("Error while reading customers from file !!! ");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public Optional<Customer> save(Customer entity) throws ValidatorException {
        Optional<Customer> optional = super.save(entity);
        if (!optional.isPresent()) {
            saveToFile(entity);
        }
        return optional;
    }

    private void saveToFile(Customer entity) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(
                    entity.getId() + "," + entity.getName() + "," + entity.getTelephoneNumber());
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Customer> delete(Long id) throws ValidatorException {
        if (id == null) {
            throw new IllegalArgumentException(" id can't be null !!");
        } else if (!super.entities.containsKey(id)) {
            throw new ValidatorException("client list doesn't contain id: " + id + "\nPlease enter a valid id!\n");

        }
        Optional<Customer> optional = super.delete(id);
        if (optional.isPresent()) {
            updateDataInFile();
        }

        return Optional.empty();
    }


    @Override
    public Optional<Customer> update(Customer customer) throws ValidatorException {
        if (customer.getId() == null) {
            throw new IllegalArgumentException("id can't be null !!!");
        } else if (!super.entities.containsKey(customer.getId())) {
            throw new ValidatorException("customer list doesn't contain id: " + customer.getId() + "\nPlease enter a valid id!\n");
        }
        Optional<Customer> optional = super.update(customer);
        if (optional.isPresent()) {
            updateDataInFile();
        }
        return optional;

    }

    private Optional<Customer> updateDataInFile() {
        Path path = Paths.get(fileName);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            PrintWriter writer = new PrintWriter(fileName);
            writer.print("");
            writer.close();
            findAll().forEach(client1 -> {
                try {
                    bufferedWriter.write(client1.getId() + "," + client1.getName() + "," + client1.getTelephoneNumber());
                    bufferedWriter.newLine();
                } catch (IOException ex) {
                    ex.getLocalizedMessage();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


}
