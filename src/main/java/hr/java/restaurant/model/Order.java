package hr.java.restaurant.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents an order in the restaurant system.
 * This class stores the details of a customer's order, including the restaurant, the meals ordered, the deliverer,
 * and the scheduled delivery date and time.
 */
public class Order {

    /** The restaurant associated with the order. */
    private Restaurant restaurant;

    /** The array of meals included in the order. */
    private List<Meal> meals;

    /** The deliverer assigned to deliver the order. */
    private Deliverer deliverer;

    /** The scheduled date and time for the delivery of the order. */
    private LocalDateTime deliveryDateAndTime;

    /**
     * Constructs a new {@code Order} with the specified restaurant, meals, deliverer, and delivery date/time.
     *
     * @param restaurant the restaurant fulfilling the order.
     * @param meals the meals included in the order.
     * @param deliverer the deliverer assigned to the order.
     * @param deliveryDateAndTime the date and time when the order will be delivered.
     */
    public Order(Restaurant restaurant, List<Meal> meals, Deliverer deliverer, LocalDateTime deliveryDateAndTime) {
        this.restaurant = restaurant;
        this.meals = meals;
        this.deliverer = deliverer;
        this.deliveryDateAndTime = deliveryDateAndTime;
    }


    public Restaurant getRestaurant() {
        return restaurant;
    }


    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }


    public List<Meal> getMeals() {
        return meals;
    }


    public void setMeals(Meal[] meals) {
        this.meals = List.of(meals);
    }


    public Deliverer getDeliverer() {
        return deliverer;
    }


    public void setDeliverer(Deliverer deliverer) {
        this.deliverer = deliverer;
    }


    public LocalDateTime getDeliveryDateAndTime() {
        return deliveryDateAndTime;
    }


    public void setDeliveryDateAndTime(LocalDateTime deliveryDateAndTime) {
        this.deliveryDateAndTime = deliveryDateAndTime;
    }
}
