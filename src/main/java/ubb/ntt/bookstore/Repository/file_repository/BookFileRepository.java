package ubb.ntt.bookstore.Repository.file_repository;

import ubb.ntt.bookstore.Domain.Book;
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
 * SellDatabaseRepository class for CRUD operation on a file repository for a book object .
 *
 * @author Adela
 */
public class BookFileRepository extends InMemoryRepository<Long, Book> {
    private String fileName;

    public BookFileRepository(Validator<Book> validator, String fileName) {
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
                String author = items.get(2);
                Integer year = Integer.valueOf(items.get(3));
                String type = items.get(4);


                Book book = new Book(name, author, year, type);
                book.setId(id);

                super.save(book);
            });
        } catch (ValidatorException e) {
            System.out.println("Error while reading books from file !!! ");
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
           e.getMessage();
        }


    }

    @Override
    public Optional<Book> save(Book entity) {
        Optional<Book> optional = super.save(entity);

        if (optional.isPresent()) {
            return optional;
        }
        saveToFile(entity);
        return Optional.empty();
    }

    private void saveToFile(Book entity) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(
                    entity.getId() + "," + entity.getName() + "," + entity.getAuthor() + "," + entity.getYear() + "," + entity.getType());
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.getLocalizedMessage();
        }
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
            updateDataInFile();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Book> update(Book book) throws ValidatorException {
        Optional<Book> optional = super.update(book);
        if (book.getId() == null) {
            throw new IllegalArgumentException("id can't be null !!!");
        } else if (!super.entities.containsKey(book.getId())) {
            throw new ValidatorException("books list doesn't contain id: " + book.getId() + "\nPlease enter a valid id!\n");
        }

        if (optional.isPresent()) {
            updateDataInFile();
            return Optional.empty();
        }

        return optional;

    }

    private Optional<Book> updateDataInFile() {
        Path path = Paths.get(fileName);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {

            PrintWriter writer = new PrintWriter(fileName);
            writer.print("");
            writer.close();
            findAll().forEach(book -> {
                try {
                    bufferedWriter.write(book.getId() + "," + book.getName() + "," + book.getAuthor() + "," + book.getYear() + "," + book.getType());
                    bufferedWriter.newLine();
                } catch (IOException x) {
                    x.getLocalizedMessage();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }


}

