package hr.java.restaurant.model;

import java.util.Objects;

/**
 * Represents a chef in the restaurant, inheriting basic properties from the {@link Person} class.
 * A chef has a contract and a bonus associated with their position.
 */
public class Chef extends Person {
    private Contract contract;
    private Bonus bonus;

    /**
     * Private constructor to create a {@link Chef} object with the specified first name, last name, contract, and bonus.
     * This constructor is used by the Builder class to instantiate a Chef.
     *
     * @param firstName The first name of the chef.
     * @param lastName  The last name of the chef.
     * @param contract  The contract associated with the chef.
     * @param bonus     The bonus associated with the chef.
     */
    private Chef(String firstName, String lastName, Contract contract, Bonus bonus) {
        super(firstName, lastName);
        this.contract = contract;
        this.bonus = bonus;
    }


    public Contract getContract() {
        return contract;
    }


    public void setContract(Contract contract) {
        this.contract = contract;
    }


    public Bonus getBonus() {
        return bonus;
    }


    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    /**
     * Builder class for constructing {@link Chef} instances.
     * The builder pattern is used to provide a flexible and readable way of creating Chef objects.
     */
    public static class Builder {
        private String firstName;
        private String lastName;
        private Contract contract;
        private Bonus bonus;

        /**
         * Sets the first name of the chef.
         *
         * @param firstName The first name of the chef.
         * @return The builder instance, for method chaining.
         */
        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        /**
         * Sets the last name of the chef.
         *
         * @param lastName The last name of the chef.
         * @return The builder instance, for method chaining.
         */
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        /**
         * Sets the contract associated with the chef.
         *
         * @param contract The contract of the chef.
         * @return The builder instance, for method chaining.
         */
        public Builder contract(Contract contract) {
            this.contract = contract;
            return this;
        }

        /**
         * Sets the bonus associated with the chef.
         *
         * @param bonus The bonus of the chef.
         * @return The builder instance, for method chaining.
         */
        public Builder bonus(Bonus bonus) {
            this.bonus = bonus;
            return this;
        }

        /**
         * Builds a new {@link Chef} instance with the values provided through the builder.
         *
         * @return A new {@link Chef} object.
         */
        public Chef build() {
            return new Chef(firstName, lastName, contract, bonus);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chef chef = (Chef) o;
        return Objects.equals(contract, chef.contract) && Objects.equals(bonus, chef.bonus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contract, bonus);
    }
}
