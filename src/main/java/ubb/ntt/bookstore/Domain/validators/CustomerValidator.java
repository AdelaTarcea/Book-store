package ubb.ntt.bookstore.Domain.validators;

import ubb.ntt.bookstore.Domain.Customer;

/**
 * CustomerValidator class validates a customer object.
 *
 * @author adela.
 */
public class CustomerValidator implements Validator<Customer> {

    @Override
    public void validate(Customer entity) throws ValidatorException {

        StringBuilder errors = new StringBuilder();

        if (entity.getName().equals(" ")) {
            errors.append("Customer's name must not be  empty/null.\n");
        }
        if(!entity.getName().matches("[a-zA-Z ]*")){
            errors.append("Customer's name must contains only letters. \n");
        }
        if (!entity.getTelephoneNumber().matches("\\d{10}")) {
            errors.append("Wrong telephone number.\n");
        }


        if (errors.length() > 0) {
            throw new ValidatorException(errors.toString());
        }

    }
}

