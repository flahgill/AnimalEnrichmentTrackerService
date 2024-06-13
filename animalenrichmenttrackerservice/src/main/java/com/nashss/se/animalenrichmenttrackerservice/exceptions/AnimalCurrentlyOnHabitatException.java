package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class AnimalCurrentlyOnHabitatException extends RuntimeException {
    private static final long serialVersionUID = 6548919239869687844L;

    /**
     * Exception with no message or cause.
     */
    public AnimalCurrentlyOnHabitatException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public AnimalCurrentlyOnHabitatException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public AnimalCurrentlyOnHabitatException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public AnimalCurrentlyOnHabitatException(String message, Throwable cause) {
        super(message, cause);
    }
}
