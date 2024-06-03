package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class EnrichmentActivityNotOnHabitatException extends RuntimeException {
    private static final long serialVersionUID = -1447221080570314906L;

    /**
     * Exception with no message or cause.
     */
    public EnrichmentActivityNotOnHabitatException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public EnrichmentActivityNotOnHabitatException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public EnrichmentActivityNotOnHabitatException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public EnrichmentActivityNotOnHabitatException(String message, Throwable cause) {
        super(message, cause);
    }
}
