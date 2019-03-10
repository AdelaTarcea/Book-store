package ubb.ntt.bookstore.Repository.paging.implementation;

import ubb.ntt.bookstore.Domain.BaseEntity;
import ubb.ntt.bookstore.Repository.paging.Page;
import ubb.ntt.bookstore.Repository.paging.Pageable;
import ubb.ntt.bookstore.Repository.paging.PagingRepository;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Stream;

public interface PagingRepositoryExtended<ID extends Serializable,
        T extends BaseEntity<ID>>
        extends PagingRepository<ID, T> {

    default Page<T> findAll(Collection<T> collection, Pageable pageable) {
        int startIndex = pageable.getPageNumber() * pageable.getPageSize();
        Stream<T> res = collection.stream()
                .skip(startIndex)
                .limit(pageable.getPageSize());
        return new PageImpl<>(pageable, res);
    }
}