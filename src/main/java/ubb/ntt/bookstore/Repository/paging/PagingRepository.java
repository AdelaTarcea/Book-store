package ubb.ntt.bookstore.Repository.paging;

import ubb.ntt.bookstore.Domain.BaseEntity;
import ubb.ntt.bookstore.Repository.Repository;

import java.io.Serializable;
import java.util.Collection;

/**
 * author: Adela
 */
public interface PagingRepository<ID extends Serializable,
        T extends BaseEntity<ID>> extends Repository<ID, T> {

    Page<T> findAll( Pageable pageable);

}
