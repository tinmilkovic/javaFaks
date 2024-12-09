package hr.java.restaurant.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a meal in the restaurant's menu.
 * A meal has a name, category, list of ingredients, and a price.
 * This class also allows calculating the total caloric value of the meal.
 */
public class Meal {
    private String name;
    private Category category;
    private Set<Ingredient> ingredients;
    private BigDecimal price;

    /**
     * Constructs a new {@link Meal} with the specified name, category, ingredients, and price.
     *
     * @param name        The name of the meal.
     * @param category    The category of the meal (e.g., vegetarian, meat-based, etc.).
     * @param ingredients The set of ingredients used in the meal.
     * @param price       The price of the meal.
     */
    public Meal(String name, Category category, Set<Ingredient> ingredients, BigDecimal price) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients != null ? ingredients : new HashSet<>();
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients != null ? ingredients : new HashSet<>();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Calculates the total caloric value of the meal by summing the caloric values of all its ingredients.
     *
     * @return The total caloric value of the meal.
     */
    public BigDecimal getTotalCalories() {
        BigDecimal totalCalories = BigDecimal.ZERO;
        for (Ingredient ingredient : ingredients) {
            if (ingredient != null) {
                totalCalories = totalCalories.add(ingredient.getKcal());
            }
        }
        return totalCalories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return Objects.equals(name, meal.name) && Objects.equals(category, meal.category) && Objects.equals(ingredients, meal.ingredients) && Objects.equals(price, meal.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, ingredients, price);
    }
}
