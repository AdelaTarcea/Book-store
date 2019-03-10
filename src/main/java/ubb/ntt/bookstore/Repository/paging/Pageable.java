package ubb.ntt.bookstore.Repository.paging;

/**
 * @author Adela.
 */
public interface Pageable {
    /**
     * @return page number; page numbers start at 0.
     */
    int getPageNumber();

    /**
     * @return the number of elements in a page.
     */
    int getPageSize();
}
