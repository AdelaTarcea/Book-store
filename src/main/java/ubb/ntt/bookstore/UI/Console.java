package ubb.ntt.bookstore.UI;

import ubb.ntt.bookstore.Domain.Book;
import ubb.ntt.bookstore.Domain.Customer;
import ubb.ntt.bookstore.Domain.Sell;
import ubb.ntt.bookstore.Domain.SellViewModel;
import ubb.ntt.bookstore.Domain.validators.ValidatorException;
import ubb.ntt.bookstore.Service.BookService;
import ubb.ntt.bookstore.Service.CustomerService;
import ubb.ntt.bookstore.Service.SellService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Console {
    private BookService bookService;
    private CustomerService customerService;
    private SellService sellService;

    public Console(BookService bookService, CustomerService customerService, SellService sellService) {
        this.bookService = bookService;
        this.customerService = customerService;
        this.sellService = sellService;
    }

    //............................................BOOKS ....................................
    private Optional<Book> readBook() {
        System.out.println("Read book :\n Id \n name \n author\n year\n type \n");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {

            Long id = Long.valueOf(bufferRead.readLine());
            String name = bufferRead.readLine();
            String author = bufferRead.readLine();// ...
            Integer year = Integer.valueOf(bufferRead.readLine());
            String category = bufferRead.readLine();// ...


            Book book = new Book(name, author, year, category);
            book.setId(id);

            return Optional.ofNullable(book);
        } catch (Exception ex) {
            System.out.println("Error while reading book.");
            return Optional.empty();
        }

    }

    private void addBooks() {

        Optional<Book> book = readBook();
        if (book.isPresent()) {
            try {
                bookService.addBook(book.get());
            } catch (ValidatorException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private void deleteBook() {
        System.out.println("Id:");
        Scanner scanner = new Scanner(System.in);
        long id = scanner.nextLong();

        try {
            bookService.deleteBook(id);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateBooks() {

        Optional<Book> book = readBook();
        if (book.isPresent()) {
            try {
                bookService.updateBook(book.get());
            } catch (ValidatorException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
        }
    }

    private void printAllBooks() {
        Set<Book> books = bookService.getAllBooks();
        try {
            books.stream().forEach(System.out::println);
        } catch (ValidatorException e) {
            System.out.println("Error while reading books from file !!! ");
            System.out.println(e.getMessage());
        }

    }

    private void printBooksWithPaging() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter page size: ");
        int size = scanner.nextInt();
        bookService.setPageSize(size);

        System.out.println("enter 'n' - for next; 'x' - for exit: ");

        while (true) {
            String cmd = scanner.next();
            if (cmd.equals("x")) {
                System.out.println("exit");
                break;
            }
            if (!cmd.equals("n")) {
                System.out.println("this option is not yet implemented");
                continue;
            }

            Set<Book> books = bookService.getNextBooks();
            if (books.size() == 0) {
                System.out.println("no more books");
                break;
            }
            books.forEach(book -> System.out.println(book));
        }
        bookService.setPage(0);
    }


    // ...........................................CUSTOMERS ...................................
    private Optional<Customer> readCustomer() {
        System.out.println("Read customer: \n id \n name\n telephone \n");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            Long id = Long.valueOf(bufferRead.readLine());
            String name = bufferRead.readLine();
            String telephone = bufferRead.readLine();

            Customer customer = new Customer(name, telephone);
            customer.setId(id);
            return Optional.ofNullable(customer);
        } catch (Exception ex) {
            System.out.println("Error while reading customer");
            return Optional.empty();
        }

    }

    private void addCustomer() {

        Optional<Customer> optional = readCustomer();

        if (optional.isPresent()) {
            try {
                customerService.addClient(optional.get());
            } catch (ValidatorException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void deleteCustomer() {
        System.out.println("Id:");
        Scanner scanner = new Scanner(System.in);
        long id = scanner.nextLong();

        try {
            customerService.deleteClient(id);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateCustomer() {

        Optional<Customer> optional = readCustomer();
        if (optional.isPresent()) {
            try {
                customerService.updateClient(optional.get());
            } catch (ValidatorException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void printAllCustomers() {
        Set<Customer> customers = customerService.getAllClients();
        customers.stream().forEach(System.out::println);
    }

    private void printClientsWithPaging() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter page size: ");
        int size = scanner.nextInt();
        customerService.setPageSize(size);

        System.out.println("enter 'n' - for next; 'x' - for exit: ");

        while (true) {
            String cmd = scanner.next();
            if (cmd.equals("x")) {
                System.out.println("exit");
                break;
            }
            if (!cmd.equals("n")) {
                System.out.println("this option is not yet implemented");
                continue;
            }

            Set<Customer> customers = customerService.getNextClients();
            if (customers.size() == 0) {
                System.out.println("no more customers");
                break;
            }
            customers.forEach(customer -> System.out.println(customer));

        }
        customerService.setPage(0);

    }


// ...........................................SELLS .....................................

    private Optional<Sell> readSell() {
        System.out.println("Read sell: \n sell Id, book id\n , client id \n");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {

            Long id = Long.valueOf(bufferRead.readLine());
            Long bookId = Long.valueOf(bufferRead.readLine());
            Long clientId = Long.valueOf(bufferRead.readLine());
            Double price = Double.valueOf(bufferRead.readLine());
            Date date = new Date();


            Sell sell = new Sell(bookId, clientId, price);
            sell.setId(id);
            sell.setDate(sell.convertDateToString(date));

            return Optional.ofNullable(sell);

        } catch (Exception ex) {
            System.out.println("Error while reading .");
            return Optional.empty();
        }
    }

    private void addSell() {

        Optional<Sell> optional = readSell();
        if (optional.isPresent()) {
            try {
                sellService.addSell(optional.get().getId(), optional.get().getBookId(), optional.get().getClientId(), optional.get().getPrice(), optional.get().getDate());
            } catch (ValidatorException e) {
                System.out.print(e.getMessage());
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
        }
    }

    private void deleteSell() {
        System.out.println("Id:");
        Scanner scanner = new Scanner(System.in);
        long id = scanner.nextLong();

        try {
            sellService.deleteSell(id);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateSell() {

        Optional<Sell> optional = readSell();
        try {
            sellService.updateSell(optional.get());
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printAllSells() {
        Set<SellViewModel> sells = sellService.getAllSells();
        sells.forEach(System.out::println);
    }

    private void printSellWithPaging() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter page size: ");
        int size = scanner.nextInt();
        sellService.setPageSize(size);

        System.out.println("enter 'n' - for next; 'x' - for exit: ");

        while (true) {
            String cmd = scanner.next();
            if (cmd.equals("x")) {
                System.out.println("exit");
                break;
            }
            if (!cmd.equals("n")) {
                System.out.println("this option is not yet implemented");
                continue;
            }

            Set<Sell> sells = sellService.getNextSells();
            if (sells.size() == 0) {
                System.out.println("no more sells");
                break;
            }
            sells.forEach(sell -> System.out.println(sell));
        }
        sellService.setPage(0);
    }


    // ................others ...............

    private void bestSellAuthor() {
        Map<String, Integer> nrOfBooksForEveryAuthor = sellService.nrOfBooksForEveryAuthor();
        System.out.print("Number of books for every author : " + nrOfBooksForEveryAuthor);
    }


    private void booksSellToClient() {
        System.out.println("client id:");
        Scanner scanner = new Scanner(System.in);
        long id = scanner.nextLong();

        Set<Book> books = sellService.booksSellToClient(id);
        books.stream().forEach(System.out::println);
    }

    private void filterBooksByName() {
        System.out.println("Enter book name :");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();
        Set<Book> filterBooks = bookService.filterBooksByName(name);

        filterBooks.stream().forEach(System.out::println);
    }

    private void filterBooksByYear() {
        System.out.println("Enter year :");
        Scanner scanner = new Scanner(System.in);
        int year = scanner.nextInt();
        List<Book> filterBooks = bookService.filterBooksByYear(year);
        filterBooks.stream().forEach(System.out::println);

    }

    public void filterAndSortBooks() {
        System.out.println("Enter year :");
        Scanner scanner = new Scanner(System.in);
        int year = scanner.nextInt();
        List<Book> filterBooks = bookService.filterAndSortBooks(year);
        filterBooks.stream().forEach(System.out::println);

    }


    public void runConsole() {
        printMenu();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String option = scanner.next();
            if (option.equals("x")) {
                break;
            }
            switch (option) {
                case "1":
                    addBooks();
                    break;
                case "2":
                    printAllBooks();
                    break;
                case "3":
                    printBooksWithPaging();
                    break;
                case "4":
                    deleteBook();
                    break;
                case "5":
                    updateBooks();
                    break;
                case "6":
                    addCustomer();
                    break;
                case "7":
                    printAllCustomers();
                    break;
                case "8":
                    printClientsWithPaging();
                    break;
                case "9":
                    deleteCustomer();
                    break;
                case "10":
                    updateCustomer();
                    break;
                case "11":
                    addSell();
                    break;
                case "12":
                    printAllSells();
                    break;
                case "13":
                    printSellWithPaging();
                    break;
                case "14":
                    deleteSell();
                    break;
                case "15":
                    updateSell();
                    break;
                case "16":
                    bestSellAuthor();
                    break;
                case "17":
                    booksSellToClient();
                    break;
                case "18":
                    filterBooksByName();
                    break;
                case "19":
                    filterBooksByYear();
                    break;
                case "20":
                    filterAndSortBooks();
                    break;
                default:
                    System.out.println("this option is not yet implemented");
            }
            printMenu();
        }
    }


    private void printMenu() {
        System.out.println(
                "\n===============================================================\n" +
                        "\uD83D\uDD35 BOOKS                         \uD83D\uDD35 CLIENTS \n" +
                        "1 - Add Book                     6 - Add client\n" +
                        "2 - Print all books              7 - Print all clients\n" +
                        "3 - Print all books paginate     8 - Print all clients paginate\n" +
                        "4 - Delete book                  9 - Delete client\n" +
                        "5 - Update book                 10 - Update client\n" +
                        "\n===============================================================\n" +
                        " \uD83D\uDD35 SELLS                          \uD83D\uDD35 OTHERS  : \n" +
                        "11 -Add sell                      16 - Best sell author\n" +
                        "12 -Print all sells               17 - Books sold to a client\n" +
                        "13 -Print all sells paginate      18 - Filter books by name\n" +
                        "14 -Delete sell                   19 - Filter books by year\n" +
                        "15 -Update sell                   20 - Filter books by year sorted\n" +
                        "                                   x - Exit\n" +
                        "\n===============================================================\n" +
                        "\n Enter option ");

        System.out.print(">\t");


    }


}

