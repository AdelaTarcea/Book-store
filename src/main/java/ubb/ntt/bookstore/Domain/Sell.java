package ubb.ntt.bookstore.Domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Sell class is use for storing sell information.
 *
 * @author Adela
 */
public class Sell extends BaseEntity<Long> {
    private long bookId;
    private long clientId;
    private String date;
    private double price;

    /**
     * This is a constructor to initialize an empty sell object.
     */
    public Sell() {
    }

    /**
     * This is a constructor to initialize a sell object.
     *
     * @param bookId   an initial sell  bookId.
     * @param clientId an initial sell clientId.
     * @param price    an initial sell price
     */
    public Sell(Long bookId, Long clientId, Double price) {
        this.bookId = bookId;
        this.clientId = clientId;
        this.price = price;

    }

    /**
     * Converts  sell's {@code date } from {@code Date } type to {@code String } type.
     *
     * @param date sell's date as a Date format
     * @return date - sell's date as a String
     */
    public String convertDateToString(Date date) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return dateFormat.format(date);
    }

    /**
     * To get a sell's book object dependency.
     *
     * @return bookId
     */
    public Long getBookId() {
        return bookId;
    }

    /**
     * To set a sell's book id.
     *
     * @param bookId a book id
     */
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    /**
     * To get a sell's client id.
     *
     * @return clientId
     */
    public Long getClientId() {
        return clientId;
    }

    /**
     * To set a sell's client object dependency.
     *
     * @param clientId a client id
     */
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    /**
     * To get a sell's date.
     *
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * To set a sell's date .
     *
     * @param date a sell's date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * To get a sell's price.
     *
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * To set a sell's book price.
     *
     * @param price a book id
     */
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sell sell = (Sell) o;
        return Double.compare(sell.price, price) == 0 &&
                Objects.equals(bookId, sell.bookId) &&
                Objects.equals(clientId, sell.clientId) &&
                Objects.equals(date, sell.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, clientId, date, price);
    }

    /**
     * Display a sell object as a string.
     */
    @Override
    public String toString() {
        return "Sell{" +
                "id=" + super.getId() +
                ", bookId=" + bookId +
                ", clientId=" + clientId +
                ", date=" + date +
                ", price=" + price +
                '}';
    }
}
