package ubb.ntt.bookstore.Domain.validators;

/**
 * Interface for custom validators for a specific type.
 *
 * @author Adela.
 */
public interface Validator<T> {

    /**
     * Validate an entity of a specific type.
     *
     * @param entity -object we want to validate.
     * @throws ValidatorException if the entity is not valid
     */
    void validate(T entity) throws ValidatorException;
}
