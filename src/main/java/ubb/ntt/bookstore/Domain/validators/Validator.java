package ubb.ntt.bookstore.Domain.validators;

/**
 * Interface for custom validators for a specific type.
 *
 * @author adela.
 */
public interface Validator<T> {

    /**
     * Method to validate an entity.
     *
     * @param entity -object we want to validate.
     * @throws ValidatorException if the entity is not valid
     */
    void validate(T entity) throws ValidatorException;
}
