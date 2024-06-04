package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class EnrichmentActivityCurrentlyOnHabitatException extends RuntimeException {
    private static final long serialVersionUID = -7958360090528872974L;

    /**
     * Exception with no message or cause.
     */
    public EnrichmentActivityCurrentlyOnHabitatException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public EnrichmentActivityCurrentlyOnHabitatException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public EnrichmentActivityCurrentlyOnHabitatException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public EnrichmentActivityCurrentlyOnHabitatException(String message, Throwable cause) {
        super(message, cause);
    }
}
