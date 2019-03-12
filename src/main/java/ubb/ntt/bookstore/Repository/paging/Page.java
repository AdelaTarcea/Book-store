package ubb.ntt.bookstore.Repository.paging;


import java.util.stream.Stream;

/**
 * Interface for getting the page content for a specific type   .
 *
 */
public interface Page<T> {
    Pageable getPageable();

    Pageable nextPageable();

    Stream<T> getContent();


}