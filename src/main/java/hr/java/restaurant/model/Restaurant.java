package hr.java.restaurant.model;

import java.util.Set;

/**
 * Represents a restaurant in the system, containing details such as its name, address,
 * available meals, and the personnel (chefs, waiters, deliverers) working at the restaurant.
 */
public class Restaurant {

    /** The name of the restaurant. */
    private String name;

    /** The address of the restaurant. */
    private Address address;

    /** An array of meals offered by the restaurant. */
    private Set<Meal> meals;

    /** An array of chefs working at the restaurant. */
    private Set<Chef> chefs;

    /** An array of waiters working at the restaurant. */
    private Set<Waiter> waiters;

    /** An array of deliverers employed by the restaurant. */
    private Set<Deliverer> deliverers;

    /**
     * Constructs a new {@code Restaurant} with the specified name, address, meals, chefs,
     * waiters, and deliverers.
     *
     * @param name the name of the restaurant.
     * @param address the address of the restaurant.
     * @param meals an array of meals offered by the restaurant.
     * @param chefs an array of chefs working at the restaurant.
     * @param waiters an array of waiters working at the restaurant.
     * @param deliverers an array of deliverers employed by the restaurant.
     */
    public Restaurant(String name, Address address, Set<Meal> meals, Set<Chef> chefs, Set<Waiter> waiters, Set<Deliverer> deliverers) {
        this.name = name;
        this.address = address;
        this.meals = meals;
        this.chefs = chefs;
        this.waiters = waiters;
        this.deliverers = deliverers;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Address getAddress() {
        return address;
    }


    public void setAddress(Address address) {
        this.address = address;
    }


    public Set<Meal> getMeals() {
        return meals;
    }


    public void setMeals(Set<Meal> meals) {
        this.meals = meals;
    }


    public Set<Chef> getChefs() {
        return chefs;
    }


    public void setChefs(Set<Chef> chefs) {
        this.chefs = chefs;
    }


    public Set<Waiter> getWaiters() {
        return waiters;
    }

    public void setWaiters(Set<Waiter> waiters) {
        this.waiters = waiters;
    }


    public Set<Deliverer> getDeliverers() {
        return deliverers;
    }


    public void setDeliverers(Set<Deliverer> deliverers) {
        this.deliverers = deliverers;
    }
}
