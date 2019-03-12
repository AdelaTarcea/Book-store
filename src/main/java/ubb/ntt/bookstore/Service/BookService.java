package ubb.ntt.bookstore.Service;

import ubb.ntt.bookstore.Domain.Book;
import ubb.ntt.bookstore.Domain.validators.ValidatorException;
import ubb.ntt.bookstore.Repository.Repository;
import ubb.ntt.bookstore.Repository.paging.Page;
import ubb.ntt.bookstore.Repository.paging.Pageable;
import ubb.ntt.bookstore.Repository.paging.PagingRepository;
import ubb.ntt.bookstore.Repository.paging.implementation.PageableImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * BookService provides business logic for book objects .
 *
 * @author Adela
 */
public class BookService {

    private int page = 0;
    private int size = 1;
    private Repository<Long, Book> repository;
    private PagingRepository<Long, Book> pagingRepository;


    public BookService(Repository<Long, Book> repository, PagingRepository<Long, Book> pagingRepository) {
        this.repository = repository;
        this.pagingRepository = pagingRepository;

    }

    /**
     * Add a new book.
     *
     * @param book -book to add
     */
    public void addBook(Book book) throws ValidatorException {
        repository.save(book);
    }

    /**
     * Delete a  book.
     *
     * @param id - book's id  to add
     */
    public void deleteBook(Long id) throws ValidatorException {
        repository.delete(id);
    }

    /**
     * Update a  book.
     *
     * @param book- book to update
     */
    public void updateBook(Book book) throws ValidatorException {
        repository.update(book);
    }

    /**
     * Display a set of  books .
     *
     * @return books- a set of books
     */
    public Set<Book> getAllBooks() {
        Iterable<Book> books = repository.findAll();
        return StreamSupport.stream(books.spliterator(), false).collect(Collectors.toSet());
    }

    /**
     * Returns all books whose name contain the given name.
     *
     * @param bookName book name or a part of a name
     * @return a set of books whose name contains the given book name
     */
    public Set<Book> filterBooksByName(String bookName) {
        Iterable<Book> books = repository.findAll();

        Set<Book> filteredBooks;

        filteredBooks = StreamSupport.stream(books.spliterator(), false)
                .filter(book -> book.getName().contains(bookName))
                .collect(Collectors.toSet());

        return filteredBooks;
    }

    /**
     * Returns a list of books. First the books whose year is less than the given year
     * followed by the books whose year is greater than the given year.
     *
     * @param year given year
     * @return list of books which in two parts
     */
    public List<Book> filterBooksByYear(int year) {
        Iterable<Book> books = repository.findAll();

        List<Book> booksBefore;
        List<Book> booksAfter;

        booksBefore = StreamSupport.stream(books.spliterator(), false)
                .filter(book -> book.getYear() <= year)
                .collect(Collectors.toList());

        booksAfter = StreamSupport.stream(books.spliterator(), false)
                .filter(book -> book.getYear() > year)
                .collect(Collectors.toList());


        List<Book> finalList = Stream.concat(booksBefore.stream(), booksAfter.stream())
                .collect(Collectors.toList());

        return finalList;
    }

    /**
     * For a given year it returns a list of books in two parts .First
     * a sorted list of books whose year is less than the given year
     * followed by another sorted of books  list whose year is greater than the given year.
     *
     * @param year given year
     * @return list of books which in two parts
     */
    public List<Book> filterAndSortBooks(int year) {
        Iterable<Book> books = repository.findAll();

        List<Book> booksBefore;
        List<Book> booksAfter;

        booksBefore = StreamSupport.stream(books.spliterator(), false)
                .filter(book -> book.getYear() <= year)
                .sorted(Comparator.comparing(Book::getName))
                .collect(Collectors.toList());

        booksAfter = StreamSupport.stream(books.spliterator(), false)
                .filter(book -> book.getYear() > year)
                .sorted(Comparator.comparing(Book::getYear).reversed())
                .collect(Collectors.toList());


        List<Book> finalSet = Stream.concat(booksBefore.stream(), booksAfter.stream())
                .collect(Collectors.toList());

        return finalSet;
    }

    /**
     * Set the page size for paginating books
     *
     * @param size number of elements displayed on one page
     */
    public void setPageSize(int size) {
        this.size = size;
    }
    /**
     * Set the pagenumber for paginating books
     *
     * @param page - page number
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * For paginating books : gets the next page of books
     *
     * @return a set of books for the content of the next page
     */
    public Set<Book> getNextBooks() {
        Pageable pageable = new PageableImpl(this.page, this.size);
        Page<Book> bookPage = pagingRepository.findAll(pageable);
        this.page++;
        return bookPage.getContent().collect(Collectors.toSet());
    }
}


