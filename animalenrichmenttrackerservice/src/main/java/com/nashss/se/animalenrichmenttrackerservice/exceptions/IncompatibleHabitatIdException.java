package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class IncompatibleHabitatIdException extends RuntimeException {
    private static final long serialVersionUID = -1049990270003732564L;

    /**
     * Exception with no message or cause.
     */
    public IncompatibleHabitatIdException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public IncompatibleHabitatIdException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public IncompatibleHabitatIdException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public IncompatibleHabitatIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
