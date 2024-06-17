package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class IncompatibleSpeciesException extends RuntimeException {
    private static final long serialVersionUID = -3685133169521012985L;

    /**
     * Exception with no message or cause.
     */
    public IncompatibleSpeciesException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public IncompatibleSpeciesException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public IncompatibleSpeciesException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public IncompatibleSpeciesException(String message, Throwable cause) {
        super(message, cause);
    }
}
