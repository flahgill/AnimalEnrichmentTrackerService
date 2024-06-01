package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class EnrichmentActivityNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -76682998390223753L;
    /**
     * Exception with no message or cause.
     */
    public EnrichmentActivityNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public EnrichmentActivityNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public EnrichmentActivityNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public EnrichmentActivityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
