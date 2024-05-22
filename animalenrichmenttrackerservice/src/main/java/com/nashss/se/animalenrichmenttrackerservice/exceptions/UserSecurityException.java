package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class UserSecurityException extends RuntimeException {

    private static final long serialVersionUID = 3173199589614228255L;

    /**
     * Exception with no message or cause.
     */
    public UserSecurityException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public UserSecurityException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public UserSecurityException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public UserSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
