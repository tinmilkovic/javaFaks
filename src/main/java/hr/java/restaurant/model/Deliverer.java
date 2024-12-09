package hr.java.restaurant.model;

import java.util.Objects;

/**
 * Represents a deliverer, who is a type of {@link Person} with a specific contract and bonus.
 * A deliverer can be employed under a contract and may also receive a bonus.
 */
public class Deliverer extends Person {
    private Contract contract;
    private Bonus bonus;

    /**
     * Constructs a new {@link Deliverer} object with the specified first name, last name, contract, and bonus.
     *
     * @param firstName The first name of the deliverer.
     * @param lastName The last name of the deliverer.
     * @param contract The contract associated with the deliverer.
     * @param bonus The bonus associated with the deliverer.
     */
    private Deliverer(String firstName, String lastName, Contract contract, Bonus bonus) {
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
     * A builder class for creating instances of {@link Deliverer}.
     * It allows setting optional attributes like first name, last name, contract, and bonus.
     */
    public static class Builder {
        private String firstName;
        private String lastName;
        private Contract contract;
        private Bonus bonus;

        /**
         * Sets the first name of the deliverer.
         *
         * @param firstName The first name to set.
         * @return The builder instance.
         */
        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        /**
         * Sets the last name of the deliverer.
         *
         * @param lastName The last name to set.
         * @return The builder instance.
         */
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        /**
         * Sets the contract of the deliverer.
         *
         * @param contract The contract to set.
         * @return The builder instance.
         */
        public Builder contract(Contract contract) {
            this.contract = contract;
            return this;
        }

        /**
         * Sets the bonus of the deliverer.
         *
         * @param bonus The bonus to set.
         * @return The builder instance.
         */
        public Builder bonus(Bonus bonus) {
            this.bonus = bonus;
            return this;
        }

        /**
         * Builds and returns a new {@link Deliverer} object with the specified attributes.
         *
         * @return A new instance of {@link Deliverer}.
         */
        public Deliverer build() {
            return new Deliverer(firstName, lastName, contract, bonus);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deliverer deliverer = (Deliverer) o;
        return Objects.equals(contract, deliverer.contract) && Objects.equals(bonus, deliverer.bonus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contract, bonus);
    }
}
