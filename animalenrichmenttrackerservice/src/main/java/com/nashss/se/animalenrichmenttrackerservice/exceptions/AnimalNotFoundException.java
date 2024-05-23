package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class AnimalNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 5838844926061941298L;

    /**
     * Exception with no message or cause.
     */
    public AnimalNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public AnimalNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public AnimalNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public AnimalNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
