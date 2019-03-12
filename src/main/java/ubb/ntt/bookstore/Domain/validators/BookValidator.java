package ubb.ntt.bookstore.Domain.validators;

import ubb.ntt.bookstore.Domain.Book;

/**
 * BookValidator class validates a book object.
 *
 * @author Adela.
 */
public class BookValidator implements Validator<Book> {
    @Override
    public void validate(Book entity) throws ValidatorException {
        StringBuilder errors = new StringBuilder();

        if (entity.getName().equals(" ")) {
            errors.append("Book's name must not be  empty/null. \n");
        }
        if(!entity.getName().matches("[a-zA-Z ]*")){
            errors.append("Book's name must contains only letters. \n");
        }
        if (1900 > entity.getYear() || entity.getYear() > 2019) {
            errors.append( "Wrong year  \n");
        }
        if (entity.getAuthor().equals(" ")) {
            errors.append("Book's author must not be  empty/null. \n");
        }
        if(!entity.getAuthor().matches("[a-zA-Z ]*")){
            errors.append("Book's author must contains only letters. \n");
        }
        if (entity.getType().equals(" ")) {
            errors.append("Book's type must not be  empty/null. \n");
        }

        if (errors.length() > 0) {
            throw new ValidatorException(errors.toString());
        }
    }
}
