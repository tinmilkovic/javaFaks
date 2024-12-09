package hr.java.restaurant.exception;

public class DuplicateInputException extends Exception {

    // Constructor with no arguments
    public DuplicateInputException() {
        super("Duplicate found.");
    }

    // Constructor that accepts a message
    public DuplicateInputException(String message) {
        super(message);
    }

    // Constructor that accepts a message and a cause
    public DuplicateInputException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor that accepts a cause
    public DuplicateInputException(Throwable cause) {
        super(cause);
    }

    public DuplicateInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
