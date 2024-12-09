package hr.java.restaurant.model;

/**
 * Represents a contract for vegetarian meal types. Any class that implements this interface
 * must provide implementations for determining if the meal is gluten-free and whether it contains eggs.
 *
 * This interface is permitted to be implemented by {@link VegeterianMeal}.
 */
public sealed interface Vegeterian permits VegeterianMeal {

    /**
     * Determines if the vegetarian meal is gluten-free.
     *
     * @return {@code true} if the meal is gluten-free, {@code false} otherwise.
     */
    boolean isGlutenFree();

    /**
     * Returns a message indicating whether the vegetarian meal contains eggs.
     *
     * @return a message indicating whether the meal contains eggs.
     */
    String hasEggs();
}
