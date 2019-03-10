package ubb.ntt.bookstore.Domain;

/**
 * BaseEntity class provides the unique {@code id } for every object class.
 * @author Adela
 */
public class BaseEntity<ID> {
    /**
     * Generic {@code id } for every object class.
     */
    private ID id;

    /**
     * Get a generic {@code id } for every object class.
     *
     * @return {@code id }-  a unique key for every object class
     */
    public ID getId() {
        return id;
    }

    /**
     * Set a generic {@code id } for every object class.
     *
     * @param id the unique key for every object class
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * @return  a string representation of the object
     */
    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }
}

