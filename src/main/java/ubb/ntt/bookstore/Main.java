package ubb.ntt.bookstore;

import ubb.ntt.bookstore.Domain.Book;
import ubb.ntt.bookstore.Domain.Customer;
import ubb.ntt.bookstore.Domain.Sell;
import ubb.ntt.bookstore.Domain.validators.BookValidator;
import ubb.ntt.bookstore.Domain.validators.CustomerValidator;
import ubb.ntt.bookstore.Domain.validators.SellValidator;
import ubb.ntt.bookstore.Domain.validators.Validator;
import ubb.ntt.bookstore.Repository.Repository;
import ubb.ntt.bookstore.Repository.database_repository.BookDatabaseRepository;
import ubb.ntt.bookstore.Repository.database_repository.CustomerDatabaseRepository;
import ubb.ntt.bookstore.Repository.database_repository.SellDatabaseRepository;
import ubb.ntt.bookstore.Repository.file_repository.BookFileRepository;
import ubb.ntt.bookstore.Repository.file_repository.CustomerFileRepository;
import ubb.ntt.bookstore.Repository.file_repository.SellFileRepository;
import ubb.ntt.bookstore.Repository.in_memory.InMemoryRepository;
import ubb.ntt.bookstore.Repository.paging.PagingRepository;
import ubb.ntt.bookstore.Repository.xml_repository.BookRepository;
import ubb.ntt.bookstore.Repository.xml_repository.CustomerRepository;
import ubb.ntt.bookstore.Repository.xml_repository.SellRepository;
import ubb.ntt.bookstore.Service.BookService;
import ubb.ntt.bookstore.Service.CustomerService;
import ubb.ntt.bookstore.Service.SellService;
import ubb.ntt.bookstore.UI.Console;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String args[]) {
        try {
            System.out.println(new File(".").getCanonicalPath());
        } catch (IOException e) {
            e.getMessage();
        }


        Scanner scanner = new Scanner(System.in);
        printMenu();
        while (true) {
            String option = scanner.next();
            if (option.equals("x")) {
                break;
            }
            switch (option) {
                case "1":
                    fileRepo();
                    break;
                case "2":
                    memoryRepo();
                    break;
                case "3":
                    XMLRepo();
                    break;
                case "4":
                    DATABASE_Repo();
                    break;
                default:
                    System.out.println("this option is not yet implemented");
            }
            printMenu();
        }
    }

    private static void printMenu() {
        System.out.println(
                "\n===============================================\n" +
                        "Choose the type of repository you want to use : \n" +
                        "1 - FileRepo\n" +
                        "2 - InMemoryRepo\n" +
                        "3 - XMLRepo\n" +
                        "4 - DATABASE_Repo\n" +
                        "x - Exit\n" +
                        "===============================================\n");

    }

    public static void fileRepo() {
        //in file repo with the Documents
        Validator<Book> bookValidator = new BookValidator();
        Validator<Customer> clientValidator = new CustomerValidator();
        Validator<Sell> sellingsValidator = new SellValidator();

        Repository<Long, Book> bookRepository = new BookFileRepository(bookValidator, "./data/books");
        Repository<Long, Customer> clientRepository = new CustomerFileRepository(clientValidator, "./data/clients");
        Repository<Long, Sell> sellingsRepository = new SellFileRepository(sellingsValidator, "./data/sells");

        PagingRepository<Long, Book> bookFilePagingRepository= new BookFileRepository(bookValidator, "./data/books");
        PagingRepository<Long, Customer> customerFilePagingRepository = new CustomerFileRepository(clientValidator, "./data/clients");
        PagingRepository<Long, Sell> sellFilePagingRepository = new SellFileRepository(sellingsValidator, "./data/sells");



        BookService bookService = new BookService(bookRepository, bookFilePagingRepository);
        CustomerService customerService = new CustomerService(clientRepository,customerFilePagingRepository);
        SellService sellService = new SellService(sellingsRepository, bookRepository, clientRepository,sellFilePagingRepository);

        Console console = new Console(bookService, customerService, sellService);
        console.runConsole();
    }

    public static void memoryRepo() {
        //in-memory repo
        Validator<Book> bookValidator = new BookValidator();
        Validator<Customer> clientValidator = new CustomerValidator();
        Validator<Sell> sellValidator = new SellValidator();


        Repository<Long, Book> bookRepository = new InMemoryRepository<>(bookValidator);
        Repository<Long, Customer> clientRepository = new InMemoryRepository<>(clientValidator);
        Repository<Long, Sell> sellRepository = new InMemoryRepository<>(sellValidator);


        PagingRepository<Long, Book> bookPagingRepository = new InMemoryRepository<>(bookValidator);
        PagingRepository<Long, Customer> customerPagingRepository = new InMemoryRepository<>(clientValidator);
        PagingRepository<Long, Sell> sellPagingRepository = new InMemoryRepository<>(sellValidator);


        BookService bookService = new BookService(bookRepository, bookPagingRepository);
        CustomerService customerService = new CustomerService(clientRepository,customerPagingRepository);
        SellService sellService = new SellService(sellRepository, bookRepository, clientRepository,sellPagingRepository);

        Console console = new Console(bookService, customerService, sellService);
        console.runConsole();
    }


    public static void XMLRepo() {

        Validator<Book> bookValidator = new BookValidator();
        Validator<Customer> clientValidator = new CustomerValidator();
        Validator<Sell> sellValidator = new SellValidator();


        Repository<Long, Book> bookRepository = new BookRepository(bookValidator, "./data/bookstore.xml");
        Repository<Long, Customer> clientRepository = new CustomerRepository(clientValidator, "./data/clientstore.xml");
        Repository<Long, Sell> sellRepository = new SellRepository(sellValidator, "./data/sells.xml");

        PagingRepository<Long, Book> bookPagingRepository = new BookRepository(bookValidator, "./data/bookstore.xml");
        PagingRepository<Long, Customer> customerPagingRepository = new CustomerRepository(clientValidator, "./data/clientstore.xml");
        PagingRepository<Long, Sell> sellPagingRepository = new SellRepository(sellValidator, "./data/sells.xml");


        BookService bookService = new BookService(bookRepository, bookPagingRepository);
        CustomerService customerService = new CustomerService(clientRepository,customerPagingRepository);
        SellService sellService = new SellService(sellRepository, bookRepository, clientRepository,sellPagingRepository);

        Console console = new Console(bookService, customerService, sellService);
        console.runConsole();
    }

    public static void DATABASE_Repo() {

        Validator<Book> bookValidator = new BookValidator();
        Validator<Customer> clientValidator = new CustomerValidator();
        Validator<Sell> sellsValidator = new SellValidator();


        Repository<Long, Book> bookRepository = new BookDatabaseRepository(bookValidator);
        Repository<Long, Customer> clientRepository = new CustomerDatabaseRepository(clientValidator);
        Repository<Long, Sell> sellsRepository = new SellDatabaseRepository(sellsValidator);

        PagingRepository<Long, Book> bookPagingRepository = new BookDatabaseRepository(bookValidator);
        PagingRepository<Long, Customer> customerPagingRepository = new CustomerDatabaseRepository(clientValidator);
        PagingRepository<Long, Sell> sellPagingRepository = new SellDatabaseRepository(sellsValidator);



        BookService bookService = new BookService(bookRepository, bookPagingRepository);
        CustomerService customerService = new CustomerService(clientRepository,customerPagingRepository);
        SellService sellService = new SellService(sellsRepository, bookRepository, clientRepository,sellPagingRepository);

        Console console = new Console(bookService, customerService, sellService);
        console.runConsole();
    }


}
