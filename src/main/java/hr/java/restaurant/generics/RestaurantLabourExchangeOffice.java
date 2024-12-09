package hr.java.restaurant.generics;

import hr.java.restaurant.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic labour exchange office for restaurant employees.
 * This class works with objects of type Restaurant or its subclasses.
 *
 * @param <T> Type of the restaurant (must extend Restaurant).
 */
public class RestaurantLabourExchangeOffice<T extends Restaurant> {

    private final List<T> restaurants;

    /**
     * Constructor to initialize the office with a list of restaurants.
     *
     * @param restaurants List of restaurants to manage.
     */
    public RestaurantLabourExchangeOffice(List<T> restaurants) {
        this.restaurants = new ArrayList<>(restaurants);
    }

    /**
     * Returns the list of restaurants managed by this office.
     *
     * @return List of restaurants.
     */
    public List<T> getRestaurants() {
        return new ArrayList<>(restaurants);
    }
}
