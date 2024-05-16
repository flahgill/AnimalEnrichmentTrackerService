package com.nashss.se.animalenrichmenttrackerservice.exceptions;

/**
 * Exception to throw when provided value has an invalid character value.
 */
public class InvalidCharacterException extends RuntimeException {

    private static final long serialVersionUID = 1916540614811022002L;

    /**
     * Exception with no message or cause.
     */
    public InvalidCharacterException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public InvalidCharacterException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public InvalidCharacterException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public InvalidCharacterException(String message, Throwable cause) {
        super(message, cause);
    }
}
