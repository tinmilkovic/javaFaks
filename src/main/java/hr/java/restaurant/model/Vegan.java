package hr.java.restaurant.model;

/**
 * The {@code Vegan} interface defines the common behavior for vegan meals.
 * It is implemented by meal classes that adhere to vegan dietary restrictions.
 * Any class implementing this interface should define whether the meal is gluten-free and steamed.
 */
public sealed interface Vegan permits VeganMeal {

    /**
     * Determines whether the meal is gluten-free.
     *
     * @return {@code true} if the meal is gluten-free, otherwise {@code false}
     */
    boolean isGlutenFree();

    /**
     * Determines whether the meal is steamed.
     *
     * @return {@code true} if the meal is steamed, otherwise {@code false}
     */
    boolean isSteamed();
}
