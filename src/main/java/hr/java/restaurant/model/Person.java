package hr.java.restaurant.model;

/**
 * Represents a person in the restaurant system.
 * This is an abstract class that serves as a base for other person-related entities such as
 * {@link Chef} and {@link Deliverer}.
 */
public abstract class Person {

    /** The first name of the person. */
    private String firstName;

    /** The last name of the person. */
    private String lastName;

    /**
     * Constructs a new {@code Person} with the specified first name and last name.
     *
     * @param firstName the first name of the person.
     * @param lastName the last name of the person.
     */
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstAndLastName() {
        return getFirstName() + " " + getLastName();
    }
}
