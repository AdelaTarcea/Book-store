package ubb.ntt.bookstore.Domain.validators;

/**
 * StoreException class for custom exception .
 *
 * @author Adela.
 *
 */
public class StoreException extends RuntimeException{

    public StoreException(String message) {
        super(message);
    }

    public StoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoreException(Throwable cause) {
        super(cause);
    }
}
