package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class DuplicateAcceptableIdException extends RuntimeException {
    private static final long serialVersionUID = 6244761923897512329L;

    /**
     * Exception with no message or cause.
     */
    public DuplicateAcceptableIdException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public DuplicateAcceptableIdException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public DuplicateAcceptableIdException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public DuplicateAcceptableIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
