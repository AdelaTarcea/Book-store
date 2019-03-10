package ubb.ntt.bookstore.Service;

import ubb.ntt.bookstore.Domain.Book;
import ubb.ntt.bookstore.Domain.Customer;
import ubb.ntt.bookstore.Domain.validators.ValidatorException;
import ubb.ntt.bookstore.Repository.Repository;
import ubb.ntt.bookstore.Repository.paging.Page;
import ubb.ntt.bookstore.Repository.paging.Pageable;
import ubb.ntt.bookstore.Repository.paging.PagingRepository;
import ubb.ntt.bookstore.Repository.paging.implementation.PageableImpl;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CustomerService {
    private int page = 0;
    private int size = 1;

    private PagingRepository<Long, Customer> pagingRepository;
    private Repository<Long, Customer> repository;

    public CustomerService(Repository<Long, Customer> repository, PagingRepository<Long, Customer> pagingRepository) {
        this.repository = repository;
        this.pagingRepository=pagingRepository;
    }

    public void addClient(Customer customer) throws ValidatorException {
        repository.save(customer);
    }

    public void deleteClient(Long id) throws ValidatorException {
        repository.delete(id);
    }

    public void updateClient(Customer customer) throws ValidatorException {
        repository.update(customer);
    }

    public Set<Customer> getAllClients() {
        Iterable<Customer> clients = repository.findAll();
        return StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
    }

    /**
     * Set the page size for paginating clients
     *
     * @param size number of elements displayed on one page
     */
    public void setPageSize(int size) {
        this.size = size;
    }
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * For paginating customers : gets the next page of customers
     *
     * @return a set of customers for the content of the next page
     */
    public Set<Customer> getNextClients() {
        Pageable pageable = new PageableImpl(this.page, this.size);
        Page<Customer> customerPage = pagingRepository.findAll(pageable);
        this.page++;
        return customerPage.getContent().collect(Collectors.toSet());
    }

}
