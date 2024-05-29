package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class EnrichmentSerializationException extends RuntimeException {
    private static final long serialVersionUID = 2690273652561088697L;

    /**
     * Exception with no message or cause.
     */
    public EnrichmentSerializationException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public EnrichmentSerializationException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public EnrichmentSerializationException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public EnrichmentSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
