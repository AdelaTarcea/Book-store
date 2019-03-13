package ubb.ntt.bookstore.Domain;

import java.util.Objects;

/**
 * SellViewModel class is use to display a sold .
 *
 * @author Adela
 */
public class SellViewModel extends BaseEntity<Long> {
    private Book book;
    private Customer customer;
    private String date;
    private double price;

    /**
     * This is a constructor to initialize a sell object.
     *
     * @param book     a book object - the book to sell
     * @param customer an customer object - the customer who buys the book
     * @param price    book's sell price
     * @param date     sell's date
     */
    public SellViewModel(Book book, Customer customer, double price, String date) {
        this.book = book;
        this.customer = customer;
        this.price = price;
        this.date = date;
    }


    public Book getBook() {
        return book;
    }
    /**
     * To set a sold book.
     *
     * @param book a sold book
     */
    public void setBook(Book book) {
        this.book = book;
    }

    public Customer getCustomer() {
        return customer;
    }
    /**
     * To set a customer who bought a book .
     *
     * @param customer customer who bought a book.
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * To get sell's date.
     *
     * @return  date at which an customer  bought a book.
     */
    public String getDate() {
        return date;
    }
    /**
     * To set a date at which a book was sold.
     *
     * @param date date at which a book was sold.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * To get the price for a sold book.
     *
     * @return  price at which a book was sold.
     */
    public double getPrice() {
        return price;
    }
    /**
     * To set the price at which a book was sold.
     *
     * @param   price at which a book was sold.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SellViewModel that = (SellViewModel) o;
        return Double.compare(that.price, price) == 0 &&
                Objects.equals(book, that.book) &&
                Objects.equals(customer, that.customer) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash( book, customer, date, price);
    }

    @Override
    public String toString() {
        return "Sell{" +
                "id: " + super.getId() +
                ", " + book +
                ", " + customer +
                ", Date: " + date +
                ", Price: " + price +" lei"+
                '}';
    }
}
