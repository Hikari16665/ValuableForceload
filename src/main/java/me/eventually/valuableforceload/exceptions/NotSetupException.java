package me.eventually.valuableforceload.exceptions;

/**
 * Thrown when a type of economy is used when it hasn't setup correctly.
 * You should always catch this exception when using/creating an economy.
 */
public class NotSetupException extends RuntimeException{
    public NotSetupException(String message) {
        super(message);
    }
    public NotSetupException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotSetupException(Throwable cause) {
        super(cause);
    }
}
