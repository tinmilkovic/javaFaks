package hr.java.restaurant.model;

import java.math.BigDecimal;

/**
 * Represents a bonus value, typically used to store the bonus amount in a monetary format.
 * The bonus is stored as a {@link BigDecimal} to ensure precision in financial calculations.
 */
public record Bonus(BigDecimal bonus) {
    /**
     * Constructs a new Bonus instance.
     * This constructor is implicitly provided by the record syntax and takes a {@link BigDecimal} value for the bonus.
     *
     * @param bonus The bonus amount, represented as a {@link BigDecimal}.
     */
}
