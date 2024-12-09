package hr.java.restaurant.model;

/**
 * Represents a physical address consisting of street, house number, city, and postal code.
 * The Address class is immutable and can be constructed using the Builder pattern for flexibility.
 */
public class Address {

    private String street;
    private String houseNumber;
    private String city;
    private String postalCode;

    /**
     * Private constructor to initialize the Address object.
     * This constructor is used by the Builder class to create an Address instance.
     *
     * @param street The street name or address line.
     * @param houseNumber The house or building number.
     * @param city The city of the address.
     * @param postalCode The postal code for the address.
     */
    private Address(String street, String houseNumber, String city, String postalCode) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * The Builder class provides a flexible way to construct an Address object.
     * It allows for incremental setting of the properties of the address and ensures immutability of the Address class.
     */
    public static class Builder {
        private String street;
        private String houseNumber;
        private String city;
        private String postalCode;

        /**
         * Sets the street for the Address.
         *
         * @param street The street name or address line.
         * @return The Builder instance to allow method chaining.
         */
        public Builder street(String street) {
            this.street = street;
            return this;
        }

        /**
         * Sets the house number for the Address.
         *
         * @param houseNumber The house or building number.
         * @return The Builder instance to allow method chaining.
         */
        public Builder houseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        /**
         * Sets the city for the Address.
         *
         * @param city The city of the address.
         * @return The Builder instance to allow method chaining.
         */
        public Builder city(String city) {
            this.city = city;
            return this;
        }

        /**
         * Sets the postal code for the Address.
         *
         * @param postalCode The postal code for the address.
         * @return The Builder instance to allow method chaining.
         */
        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        /**
         * Constructs the Address object using the provided values.
         *
         * @return The newly created Address object.
         */
        public Address build() {
            return new Address(street, houseNumber, city, postalCode);
        }
    }
}
