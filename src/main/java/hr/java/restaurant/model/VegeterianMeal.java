package hr.java.restaurant.model;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Represents a vegetarian meal at the restaurant. Inherits from the {@link Meal} class
 * and implements the {@link Vegeterian} interface. This class adds a property to indicate
 * whether the meal contains eggs.
 */
public final class VegeterianMeal extends Meal implements Vegeterian {

    /** Flag indicating whether the meal contains eggs. */
    private boolean hasEggs;

    /**
     * Constructs a new {@code VegeterianMeal} with the specified name, category, ingredients, price,
     * and whether the meal contains eggs.
     *
     * @param name the name of the vegetarian meal.
     * @param category the category of the vegetarian meal.
     * @param ingredients the ingredients of the vegetarian meal.
     * @param price the price of the vegetarian meal.
     * @param hasEggs a flag indicating whether the meal contains eggs.
     */
    public VegeterianMeal(String name, Category category, Set<Ingredient> ingredients, BigDecimal price, boolean hasEggs) {
        super(name, category, ingredients, price);
        this.hasEggs = hasEggs;
    }


    public boolean getHasEggs() {
        return hasEggs;
    }


    public void setHasEggs(boolean hasEggs) {
        this.hasEggs = hasEggs;
    }

    /**
     * Determines if the vegetarian meal is gluten-free. By default, it is not gluten-free.
     *
     * @return {@code false} indicating that the meal is not gluten-free.
     */
    @Override
    public boolean isGlutenFree() {
        return false;
    }

    /**
     * Returns a message indicating whether the vegetarian meal contains eggs.
     *
     * @return a message indicating whether the meal has eggs.
     */
    @Override
    public String hasEggs() {
        if(hasEggs)
            return "Meal has eggs";
        else
            return "Meal doesn't have eggs";
    }
}
