package hr.java.restaurant.model;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Represents a meat-based meal in the restaurant.
 * This class extends {@link Meal} and implements the {@link Meat} interface.
 * A {@code MeatMeal} includes details about the meat type, and provides
 * specific implementations for checking if the meat is raw and if it comes with a side.
 */
public final class MeatMeal extends Meal implements Meat {

    /** The type of meat used in the meal. */
    private String typeOfMeat;

    /**
     * Constructs a {@code MeatMeal} with the specified name, category, ingredients, price, and type of meat.
     *
     * @param name the name of the meal.
     * @param category the category of the meal.
     * @param ingredients the ingredients of the meal.
     * @param price the price of the meal.
     * @param typeOfMeat the type of meat used in the meal.
     */
    public MeatMeal(String name, Category category, Set<Ingredient> ingredients, BigDecimal price, String typeOfMeat) {
        super(name, category, ingredients, price);
        this.typeOfMeat = typeOfMeat;
    }

    /**
     * Returns the type of meat used in the meal.
     *
     * @return the type of meat.
     */
    public String getTypeOfMeat() {
        return typeOfMeat;
    }

    /**
     * Sets the type of meat for the meal.
     *
     * @param typeOfMeat the type of meat to set.
     */
    public void setTypeOfMeat(String typeOfMeat) {
        this.typeOfMeat = typeOfMeat;
    }

    /**
     * Returns whether the meat is raw.
     * This method is overridden from the {@link Meat} interface and always returns {@code false}
     * for a {@code MeatMeal}, as the meat is assumed to be cooked.
     *
     * @return {@code false}, as meat in this meal is not raw.
     */
    @Override
    public boolean isRaw() {
        return false;
    }

    /**
     * Returns whether the meal comes with a side dish.
     * This method is overridden from the {@link Meat} interface and always returns {@code true}
     * for a {@code MeatMeal}, as it is assumed that meat meals are served with a side.
     *
     * @return {@code true}, as meat meals are assumed to come with a side dish.
     */
    @Override
    public boolean comesWithASide() {
        return true;
    }
}
