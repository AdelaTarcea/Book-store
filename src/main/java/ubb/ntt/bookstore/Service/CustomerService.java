package ubb.ntt.bookstore.Service;

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

/**
 * CustomerService provides business logic for customer objects .
 *
 * @author Adela
 */
public class CustomerService {
    private int page = 0;
    private int size = 1;

    private PagingRepository<Long, Customer> pagingRepository;
    private Repository<Long, Customer> repository;

    public CustomerService(Repository<Long, Customer> repository, PagingRepository<Long, Customer> pagingRepository) {
        this.repository = repository;
        this.pagingRepository = pagingRepository;
    }

    /**
     * Add a new customer
     *
     * @param customer a new costumer
     */
    public void addCustomer(Customer customer) throws ValidatorException {
        repository.save(customer);
    }
    /**
     * Delete customer
     *
     * @param id costumer's id
     */
    public void deleteCustomer(Long id) throws ValidatorException {
        repository.delete(id);
    }
    /**
     * update customer
     *
     * @param customer costumer to update
     */
    public void updateCustomer(Customer customer) throws ValidatorException {
        repository.update(customer);
    }

    /**
     * print all customers
     *
     * @return  a set of costumers
     */
    public Set<Customer> getAllCustomers() {
        Iterable<Customer> clients = repository.findAll();
        return StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
    }

    /**
     * Set the page size for paginating customers
     *
     * @param size number of elements displayed on one page
     */
    public void setPageSize(int size) {
        this.size = size;
    }
    /**
     * Set the page  for paginating customers
     *
     * @param page page number
     */
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
