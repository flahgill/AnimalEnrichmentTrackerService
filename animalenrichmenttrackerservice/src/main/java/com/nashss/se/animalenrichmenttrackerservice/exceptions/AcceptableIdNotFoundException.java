package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class AcceptableIdNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -8846955705300280038L;

    /**
     * Exception with no message or cause.
     */
    public AcceptableIdNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public AcceptableIdNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public AcceptableIdNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public AcceptableIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
