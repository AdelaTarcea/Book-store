package ubb.ntt.bookstore.Repository.paging.implementation;

import ubb.ntt.bookstore.Repository.paging.Page;
import ubb.ntt.bookstore.Repository.paging.Pageable;

import java.util.stream.Stream;


public class PageImpl<T> implements Page<T> {
    private Pageable pageable;
    private Stream<T> content;

    public PageImpl(Pageable pageable, Stream<T> content) {
        this.pageable = pageable;
        this.content = content;
    }

    @Override
    public Pageable getPageable() {
        return pageable;
    }

    @Override
    public Pageable nextPageable() {
        return new PageableImpl(this.pageable.getPageNumber(),
                this.pageable.getPageSize());
    }

    @Override
    public Stream<T> getContent() {
        return content;
    }
}
