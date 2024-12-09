package hr.java.restaurant.model;

import java.util.Objects;

/**
 * Represents a waiter at the restaurant. Inherits from the {@link Person} class.
 * Contains information about the waiter's contract and bonus.
 */
public class Waiter extends Person {

    /** The contract associated with the waiter. */
    private Contract contract;

    /** The bonus associated with the waiter. */
    private Bonus bonus;

    /**
     * Constructs a new {@code Waiter} with the specified first name, last name, contract, and bonus.
     *
     * @param firstName the first name of the waiter.
     * @param lastName the last name of the waiter.
     * @param contract the contract associated with the waiter.
     * @param bonus the bonus associated with the waiter.
     */
    private Waiter(String firstName, String lastName, Contract contract, Bonus bonus) {
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
     * A builder for constructing a {@code Waiter} object.
     */
    public static class Builder {

        private String firstName;
        private String lastName;
        private Contract contract;
        private Bonus bonus;

        /**
         * Sets the first name of the waiter.
         *
         * @param firstName the first name of the waiter.
         * @return the builder instance.
         */
        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        /**
         * Sets the last name of the waiter.
         *
         * @param lastName the last name of the waiter.
         * @return the builder instance.
         */
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        /**
         * Sets the contract for the waiter.
         *
         * @param contract the contract to set.
         * @return the builder instance.
         */
        public Builder contract(Contract contract) {
            this.contract = contract;
            return this;
        }

        /**
         * Sets the bonus for the waiter.
         *
         * @param bonus the bonus to set.
         * @return the builder instance.
         */
        public Builder bonus(Bonus bonus) {
            this.bonus = bonus;
            return this;
        }

        /**
         * Builds a {@code Waiter} object with the specified properties.
         *
         * @return a new {@code Waiter} object.
         */
        public Waiter build() {
            return new Waiter(firstName, lastName, contract, bonus);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Waiter waiter = (Waiter) o;
        return Objects.equals(contract, waiter.contract) && Objects.equals(bonus, waiter.bonus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contract, bonus);
    }
}
