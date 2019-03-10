package ubb.ntt.bookstore.Domain;


import java.util.Objects;

/**
 * Customer class is use for storing costumer information.
 *
 * @author adela
 */
public class Customer extends BaseEntity<Long> {
    private String name, telephoneNumber;


    /**
     * This is a constructor for initialize an empty customer object.
     */
    public Customer() {
    }

    /**
     * This is a constructor for initialize a customer object.
     *
     * @param name            an initial customer name.
     * @param telephoneNumber an initial customer telephone.
     */
    public Customer(String name, String telephoneNumber) {

        this.name = name;
        this.telephoneNumber = telephoneNumber;

    }

    /**
     * To get a customer's name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * To set a costumer's name.
     *
     * @param name a costumer's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * To get a costumer's telephone number.
     *
     * @return telephone number
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * To set a costumer's telephone number.
     *
     * @param telephoneNumber a costumer's telephone number
     */
    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(name, customer.name) &&
                Objects.equals(telephoneNumber, customer.telephoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, telephoneNumber);
    }

    /**
     * Display a costumer object as a string.
     */
    @Override
    public String toString() {
        return "Customer{" +
                "id='" + super.getId() + '\'' +
                ", name='" + name + '\'' +
                ", telephone number='" + telephoneNumber + '\'' +
                '}';
    }
}
