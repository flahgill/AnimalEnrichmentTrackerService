package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class EnrichmentNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 2810099171840335183L;

    /**
     * Exception with no message or cause.
     */
    public EnrichmentNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public EnrichmentNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public EnrichmentNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public EnrichmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
