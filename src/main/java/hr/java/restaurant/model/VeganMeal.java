package hr.java.restaurant.model;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Represents a vegan meal, which is a specific type of meal that adheres to vegan dietary requirements.
 * This class extends the {@link Meal} class and implements the {@link Vegan} interface.
 * It provides information about the portion size and specifies that the meal is gluten-free.
 */
public final class VeganMeal extends Meal implements Vegan {

    private String portionSize;

    /**
     * Constructs a new VeganMeal with the specified name, category, ingredients, price, and portion size.
     *
     * @param name        the name of the vegan meal
     * @param category    the category to which the meal belongs
     * @param ingredients the ingredients of the meal
     * @param price       the price of the meal
     * @param portionSize the portion size of the vegan meal
     */
    public VeganMeal(String name, Category category, Set<Ingredient> ingredients, BigDecimal price, String portionSize) {
        super(name, category, ingredients, price);
        this.portionSize = portionSize;
    }


    public String getPortionSize() {
        return portionSize;
    }


    public void setPortionSize(String portionSize) {
        this.portionSize = portionSize;
    }

    /**
     * Determines if the vegan meal is gluten-free.
     *
     * @return {@code true} as all vegan meals in this system are gluten-free
     */
    @Override
    public boolean isGlutenFree() {
        return true;
    }

    /**
     * Determines if the vegan meal is steamed.
     *
     * @return {@code false} since this meal type is not steamed
     */
    @Override
    public boolean isSteamed() {
        return false;
    }
}
