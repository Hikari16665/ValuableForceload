package me.eventually.valuableforceload.exceptions;

public class NotSupportedCurrentException extends RuntimeException {
    public NotSupportedCurrentException(String message) {
        super(message);
    }
    public NotSupportedCurrentException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotSupportedCurrentException(Throwable cause) {
        super(cause);
    }
}
