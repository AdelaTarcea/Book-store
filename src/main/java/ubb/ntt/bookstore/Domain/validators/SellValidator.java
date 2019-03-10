package ubb.ntt.bookstore.Domain.validators;


import ubb.ntt.bookstore.Domain.Sell;

/**
 * SellValidator class validates a sell object.
 *
 * @author adela.
 */

public class SellValidator implements Validator<Sell> {

    @Override
    public void validate(Sell entity) throws ValidatorException {

        StringBuilder errors = new StringBuilder();


        if (entity.getBookId().equals(" ")) {
            errors.append("Book's id must not be empty/null.\n");
        }

        if (entity.getClientId().equals(" ")) {
            errors.append("Customer's id  must not be empty/null.\n");
        }
        if (entity.getPrice() == 0) {
            errors.append("Price must be greater than 0.\n");
        }

        if (errors.length() > 0) {
            throw new ValidatorException(errors.toString());
        }

    }
}
