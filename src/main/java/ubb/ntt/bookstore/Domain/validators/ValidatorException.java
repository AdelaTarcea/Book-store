package ubb.ntt.bookstore.Domain.validators;

/**
 * ValidatorException class for custom validation exception for a specific type .
 *
 * @author adela.
 *
 */
public class ValidatorException extends StoreException {
    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorException(Throwable cause) {
        super(cause);
    }
}
