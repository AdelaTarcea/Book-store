package ubb.ntt.bookstore.Repository.file_repository;

import ubb.ntt.bookstore.Domain.Sell;
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
 * SellFileRepository class for CRUD operation on a file repository for a sell object .
 *
 * @author Adela
 */
public class SellFileRepository extends InMemoryRepository<Long, Sell> {

    private String fileName;

    public SellFileRepository(Validator<Sell> validator, String fileName) {
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
                Long book_id = Long.valueOf(items.get(1));
                Long client_id = Long.valueOf(items.get(2));
                Double price = Double.valueOf(items.get(3));
                String date = items.get(4);


                Sell sell = new Sell(book_id, client_id, price);
                sell.setDate(date);
                sell.setId(id);

                try {
                    super.save(sell);
                } catch (ValidatorException e) {
                    e.getLocalizedMessage();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ValidatorException e) {
            System.out.println("Error while reading sells from file !!! ");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public Optional<Sell> save(Sell entity) throws ValidatorException {
        Optional<Sell> optional = super.save(entity);

        if (optional.isPresent()) {
            return optional;
        }
        saveToFile(entity);
        return Optional.empty();
    }

    private void saveToFile(Sell entity) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(
                    entity.getId() + "," + entity.getBookId() + "," + entity.getClientId() + "," + entity.getPrice() + "," + entity.getDate());
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Sell> delete(Long id) throws ValidatorException {
        Optional<Sell> optional = super.delete(id);
        if (id == null || !super.entities.containsKey(id)) {
            throw new ValidatorException("there's no sell with the id " + id + " in the list.\nPlease enter a valid id!\n");
        }
        deleteDataFromFile(id);
        return Optional.empty();
    }

    private void deleteDataFromFile(Long id) {
        Path path = Paths.get(fileName);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            super.delete(id);
            PrintWriter writer = new PrintWriter(fileName);
            writer.print("");
            writer.close();
            findAll().forEach(sell -> {
                try {
                    bufferedWriter.write(sell.getId() + "," + sell.getBookId() + "," + sell.getClientId() + "," + sell.getPrice() + "," + sell.getDate());
                    bufferedWriter.newLine();
                } catch (IOException ex) {
                    ex.getLocalizedMessage();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Optional<Sell> update(Sell sell) throws ValidatorException {
        Optional<Sell> optional = super.update(sell);
        if (sell.getId() == null || !super.entities.containsKey(sell.getId())) {
            throw new ValidatorException("sells list doesn't contain id: " + sell.getId() + "\nPlease enter a valid id!\n");
        }
        updateDataInFile(sell);
        return Optional.empty();
    }

    private Sell updateDataInFile(Sell sell) {
        Path path = Paths.get(fileName);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            super.update(sell);
            PrintWriter writer = new PrintWriter(fileName);
            writer.print("");
            writer.close();
            findAll().forEach(s -> {
                try {
                    bufferedWriter.write(s.getId() + "," + s.getBookId() + "," + s.getClientId() + "," + sell.getPrice() + "," + sell.getDate());
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sell;
    }

}
