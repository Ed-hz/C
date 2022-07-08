package com.secure.practice.service.ex;

public class TokenNotAvailableException extends ControllerException{
    public TokenNotAvailableException() {
    }

    public TokenNotAvailableException(String message) {
        super(message);
    }

    public TokenNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenNotAvailableException(Throwable cause) {
        super(cause);
    }

    public TokenNotAvailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
