package ubb.ntt.bookstore.Repository.paging.implementation;

import ubb.ntt.bookstore.Repository.paging.Pageable;

/**
 * Defines page structure for a specific type   .

 */
public class PageableImpl implements Pageable {
    private int pageNumber;
    private int pageSize;


    public PageableImpl(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }
}