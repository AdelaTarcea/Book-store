package ubb.ntt.bookstore.Repository.in_memory;

import ubb.ntt.bookstore.Domain.BaseEntity;
import ubb.ntt.bookstore.Domain.validators.Validator;
import ubb.ntt.bookstore.Domain.validators.ValidatorException;
import ubb.ntt.bookstore.Repository.Repository;
import ubb.ntt.bookstore.Repository.paging.Page;
import ubb.ntt.bookstore.Repository.paging.Pageable;
import ubb.ntt.bookstore.Repository.paging.PagingRepository;
import ubb.ntt.bookstore.Repository.paging.implementation.PagingRepositoryExtended;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * InMemoryRepository class - repository  for generic CRUD operations for a  specific type . Store  entity in memory for a specific type.
 * @author Adela
 */
public class InMemoryRepository<ID extends Serializable, T extends BaseEntity<ID>>
        implements Repository<ID, T>, PagingRepositoryExtended<ID, T> {
    /**
     * store  entity in memory for a specific type.
     */
    protected Map<ID, T> entities;
    private Validator<T> validator;

    public InMemoryRepository(Validator<T> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public Optional<T> findOne(ID id) throws ValidatorException{
        if (id == null) {
            throw new ValidatorException("id must not be null");
        }
        return Optional.ofNullable(entities.get(id));
    }


    @Override
    public Iterable<T> findAll() {
        Set<T> allEntities = entities.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toSet());
        return allEntities;
    }

    @Override
    public Optional<T> save(T entity) throws ValidatorException {
        if (entity == null) {
            throw new ValidatorException("id null");
        }
        if (entities.containsKey(entity.getId())) {
            throw new ValidatorException("id already exist");
        }
        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<T> delete(ID id)throws ValidatorException {
        if (id == null||!entities.containsKey(id)) {
            throw new ValidatorException("there's no  id "+id+" in the list.\nPlease enter a valid id!\n");
        }
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<T> update(T entity) throws ValidatorException {
        if (entity == null) {
            throw new ValidatorException("entity must not be null");
        }
        validator.validate(entity);
        return Optional.ofNullable(entities.computeIfPresent(entity.getId(), (k, v) -> entity));
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return this.findAll(this.entities.values(), pageable);
    }
}
