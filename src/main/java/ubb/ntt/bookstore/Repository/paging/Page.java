package ubb.ntt.bookstore.Repository.paging;


import java.util.stream.Stream;

/**
 * author: Adela
 */
public interface Page<T> {
    Pageable getPageable();

    Pageable nextPageable();

    Stream<T> getContent();


}