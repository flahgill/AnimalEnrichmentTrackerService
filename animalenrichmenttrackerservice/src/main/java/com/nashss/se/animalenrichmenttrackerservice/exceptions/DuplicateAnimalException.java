package com.nashss.se.animalenrichmenttrackerservice.exceptions;

public class DuplicateAnimalException extends RuntimeException {

    private static final long serialVersionUID = 9178555850152069251L;

    /**
     * Exception with no message or cause.
     */
    public DuplicateAnimalException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public DuplicateAnimalException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public DuplicateAnimalException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public DuplicateAnimalException(String message, Throwable cause) {
        super(message, cause);
    }
}
