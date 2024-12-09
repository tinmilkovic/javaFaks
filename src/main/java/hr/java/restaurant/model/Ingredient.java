package hr.java.restaurant.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents an ingredient used in the restaurant's meals.
 * Each ingredient has a name, category, caloric value (kcal), and a method of preparation.
 */
public class Ingredient {
    private String name;
    private Category category;
    private BigDecimal kcal;
    private String preparationMethod;

    /**
     * Constructs an {@link Ingredient} with the specified name, category, caloric value, and preparation method.
     *
     * @param name              The name of the ingredient.
     * @param category          The category of the ingredient (e.g., vegetable, meat, etc.).
     * @param kcal              The caloric value of the ingredient, represented in kilocalories (kcal).
     * @param preparationMethod The method of preparation for this ingredient (e.g., boiled, fried, raw).
     */
    public Ingredient(String name, Category category, BigDecimal kcal, String preparationMethod) {
        this.name = name;
        this.category = category;
        this.kcal = kcal;
        this.preparationMethod = preparationMethod;
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


    public BigDecimal getKcal() {
        return kcal;
    }


    public void setKcal(BigDecimal kcal) {
        this.kcal = kcal;
    }


    public String getPreparationMethod() {
        return preparationMethod;
    }


    public void setPreparationMethod(String preparationMethod) {
        this.preparationMethod = preparationMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(name, that.name) && Objects.equals(category, that.category) && Objects.equals(kcal, that.kcal) && Objects.equals(preparationMethod, that.preparationMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, kcal, preparationMethod);
    }
}
