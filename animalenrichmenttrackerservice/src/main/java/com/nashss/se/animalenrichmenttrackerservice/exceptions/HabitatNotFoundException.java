package com.nashss.se.animalenrichmenttrackerservice.exceptions;

/**
 * Exception to throw when habitat is not found in database.
 */
public class HabitatNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 2689498942044566934L;

    /**
     * Exception with no message or cause.
     */
    public HabitatNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public HabitatNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public HabitatNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public HabitatNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
