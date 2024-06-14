package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class SpeciesNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 8592368775542132932L;

    /**
     * Exception with no message or cause.
     */
    public SpeciesNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public SpeciesNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public SpeciesNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public SpeciesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
