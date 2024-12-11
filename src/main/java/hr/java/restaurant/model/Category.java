package hr.java.restaurant.model;

/**
 * Represents a category within the restaurant, such as a meal type or item classification.
 * A category has a name and a description that provide details about what it encompasses.
 */
public class Category{
    private String name;
    private String description;

    /**
     * Private constructor to create a Category object with the specified name and description.
     * This constructor is used by the Builder class to instantiate a Category.
     *
     * @param name        The name of the category.
     * @param description A description of the category.
     */
    private Category(String name, String description){
        this.name = name;
        this.description = description;
    }

    /**
     * Builder class for constructing {@link Category} instances.
     * The builder pattern is used to provide a flexible and readable way of creating Category objects.
     */
    public static class Builder {
        private String name;
        private String description;

        /**
         * Sets the name of the category.
         *
         * @param name The name of the category.
         * @return The builder instance, for method chaining.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the description of the category.
         *
         * @param description The description of the category.
         * @return The builder instance, for method chaining.
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Builds a new {@link Category} instance with the values provided through the builder.
         *
         * @return A new {@link Category} object.
         */
        public Category build() {
            return new Category(name, description);
        }
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }
}
