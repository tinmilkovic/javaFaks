package hr.java.restaurant.model;

/**
 * Represents a contract for meat-based meals.
 * This interface provides methods for checking if the meat is raw and whether it comes with a side.
 * Only the {@link MeatMeal} class is permitted to implement this interface.
 */
public sealed interface Meat permits MeatMeal {

    /**
     * Checks if the meat is raw.
     *
     * @return {@code true} if the meat is raw, {@code false} otherwise.
     */
    boolean isRaw();

    /**
     * Checks if the meat comes with a side dish.
     *
     * @return {@code true} if the meat comes with a side dish, {@code false} otherwise.
     */
    boolean comesWithASide();
}
