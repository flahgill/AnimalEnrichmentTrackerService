package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class DuplicateSpeciesException extends RuntimeException {
    private static final long serialVersionUID = -7480964393699657083L;

    /**
     * Exception with no message or cause.
     */
    public DuplicateSpeciesException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public DuplicateSpeciesException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public DuplicateSpeciesException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public DuplicateSpeciesException(String message, Throwable cause) {
        super(message, cause);
    }

}
