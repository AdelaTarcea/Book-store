package ubb.ntt.bookstore.Service;

import ubb.ntt.bookstore.Domain.Book;
import ubb.ntt.bookstore.Domain.Customer;
import ubb.ntt.bookstore.Domain.Sell;
import ubb.ntt.bookstore.Domain.SellViewModel;
import ubb.ntt.bookstore.Domain.validators.ValidatorException;
import ubb.ntt.bookstore.Repository.Repository;
import ubb.ntt.bookstore.Repository.paging.Page;
import ubb.ntt.bookstore.Repository.paging.Pageable;
import ubb.ntt.bookstore.Repository.paging.PagingRepository;
import ubb.ntt.bookstore.Repository.paging.implementation.PageableImpl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SellService {
    private int page = 0;
    private int size = 1;

    private Repository<Long, Sell> repository;
    private Repository<Long, Book> bookRepository;
    private Repository<Long, Customer> costumerRepository;

    private PagingRepository<Long, Sell> pagingRepository;

    public SellService(Repository<Long, Sell> repository,
                       Repository<Long, Book> bookRepository,
                       Repository<Long, Customer> costumerRepository,
                       PagingRepository<Long, Sell> pagingRepository) {

        this.repository = repository;
        this.bookRepository = bookRepository;
        this.costumerRepository = costumerRepository;
        this.pagingRepository = pagingRepository;
    }

    public void addSell(Long id, Long bookId, Long costumerId, Double price, String date) throws ValidatorException {


        if (!bookRepository.findOne(bookId).isPresent()) {
            throw new ValidatorException("Book doesn't exist!");
        }
        if (!costumerRepository.findOne(costumerId).isPresent()) {
            throw new ValidatorException("Customer doesn't exist!");
        }
        if (repository.findOne(id).isPresent()) {
            throw new ValidatorException("already exist a sell with the id: " + id + "\nPlease enter a valid id!\n");
        }


        Sell sell = new Sell(bookId, costumerId, price);
        sell.setId(id);
        sell.setDate(date);

        repository.save(sell);


    }

    public void updateSell(Sell sell) throws ValidatorException {
        repository.update(sell);
    }

    public void deleteSell(Long id) throws ValidatorException {
        repository.delete(id);
    }

    public Map<String, Integer> nrOfBooksForEveryAuthor() {

        Set<SellViewModel> allSells = getAllSells();

        Map<String, List<SellViewModel>> sortedSell;

        sortedSell = allSells.stream()
                .distinct()
                .collect(Collectors.groupingBy(((SellViewModel x) -> x.getBook().getAuthor())));

        Map<String, Integer> maxElemList = new HashMap<>();

        sortedSell.forEach((k, v) -> maxElemList.put(k, v.size()));

        Object maxEntry = Collections.max(maxElemList.entrySet(), Map.Entry.comparingByValue()).getKey();

        System.out.println("Best selling  author: " + maxEntry);


        return maxElemList;
    }


    public Set<SellViewModel> getAllSells() {
        Iterable<Sell> sells = repository.findAll();

        Set<SellViewModel> result = new HashSet<>();

        sells.forEach(sell -> {

            Optional<Book> book = bookRepository.findOne(sell.getBookId());
            Optional<Customer> client = costumerRepository.findOne(sell.getClientId());

            SellViewModel sellViewModel = new SellViewModel(book.get(), client.get(), sell.getPrice(), sell.getDate());

            sellViewModel.setId(sell.getId());
            result.add(sellViewModel);

        });
        return StreamSupport.stream(result.spliterator(), false).collect(Collectors.toSet());


    }


    public Set<Book> booksSellToClient(Long client_id) {

        Set<Book> bookList = new HashSet<>();
        Set<Sell> sells = new HashSet<>();
        Set<Sell> sell2;
        Set<SellViewModel> sellsViewModels = getAllSells();

        sellsViewModels.forEach(sell -> {
            sell.setDate(sell.getDate());
            sells.add(new Sell(sell.getBook().getId(), sell.getCustomer().getId(), sell.getPrice()));
        });

        sell2 = sells.stream()
                .filter(sell -> sell.getClientId().equals(client_id))
                .collect(Collectors.toSet());

        sell2.forEach(sell -> {
            Optional<Book> book = bookRepository.findOne(sell.getBookId());
            bookList.add(book.get());

        });

        return StreamSupport.stream(bookList.spliterator(), false).collect(Collectors.toSet());
    }

    /**
     * Set the page number for printing sells paginate
     *
     * @param page - page number
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Set the page size for printing sells paginate
     *
     * @param size number of elements print on one page
     */
    public void setPageSize(int size) {
        this.size = size;
    }

    /**
     * For paginating sells : gets the next page of sells
     *
     * @return a set of sells for the content of the next page
     */
    public Set<Sell> getNextSells() {
        Pageable pageable = new PageableImpl(this.page, this.size);
        Page<Sell> sellPage = pagingRepository.findAll(pageable);
        this.page++;
        return sellPage.getContent().collect(Collectors.toSet());
    }
}

