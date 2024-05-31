package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class UnsuitableEnrichmentForHabitatException extends RuntimeException {
    private static final long serialVersionUID = 8781713248250698850L;

    /**
     * Exception with no message or cause.
     */
    public UnsuitableEnrichmentForHabitatException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public UnsuitableEnrichmentForHabitatException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public UnsuitableEnrichmentForHabitatException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public UnsuitableEnrichmentForHabitatException(String message, Throwable cause) {
        super(message, cause);
    }
}
