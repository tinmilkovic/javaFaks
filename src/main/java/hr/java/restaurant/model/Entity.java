package hr.java.restaurant.model;

/**
 * Represents a generic entity with a unique identifier.
 * This class serves as a base for other entities in the system
 * that require an identifier.
 */
public abstract class Entity {
    private Long id;

    /**
     * Constructs an {@link Entity} with the specified identifier.
     *
     * @param id The unique identifier for the entity.
     */
    public Entity(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }
}
