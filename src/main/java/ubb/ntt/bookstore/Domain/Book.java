package ubb.ntt.bookstore.Domain;


import java.util.Objects;

/**
 * Book class is use for storing book information.
 *
 * @author Adela
 */
public class Book extends BaseEntity<Long> {


    private String name, author, type;
    private int year;

    /**
     * This is a constructor for initialize an empty book object.
     */
    public Book() {
    }

    /**
     * This is a constructor for initialize a book object.
     *
     * @param name   an initial book name.
     * @param author an initial book author.
     * @param year   an initial book year.
     * @param type   an initial book type.
     */
    public Book(String name, String author, Integer year, String type) {

        this.name = name;
        this.author = author;
        this.year = year;
        this.type = type;
    }

    /**
     * To get a book's name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * To set a book's name.
     *
     * @param name  book's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * To get a book's author.
     *
     * @return author -  book's author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * To set a book's author.
     *
     * @param author a book author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * To get a book's type.
     *
     * @return a book's type
     */
    public String getType() {
        return type;
    }

    /**
     * To set a book's type
     *
     * @param type a book type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * To get a book's year
     *
     * @return year book's year
     */
    public int getYear() {
        return year;
    }

    /**
     * To set an book's year.
     *
     * @param year a book's year
     */
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return year == book.year &&
                Objects.equals(name, book.name) &&
                Objects.equals(author, book.author) &&
                Objects.equals(type, book.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, author, type, year);
    }

    /**
     * Display a book object as a string.
     */
    //@Override
    public String toString() {
        return "Book{" +
                "id: '" + super.getId() + '\'' +
                ", name: '" + name + '\'' +
                ", author: '" + author + '\'' +
                ", year: " + year +
                ", type: '" + type + '\'' +
                '}';

    }
}

