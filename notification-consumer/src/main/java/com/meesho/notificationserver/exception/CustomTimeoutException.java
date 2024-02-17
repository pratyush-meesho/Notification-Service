package com.meesho.notificationserver.exception;

public class CustomTimeoutException extends RuntimeException {
    public CustomTimeoutException(String message) {
        super(message);
    }

    public CustomTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
