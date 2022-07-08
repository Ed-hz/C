package com.secure.practice.service.ex;

public class CaStateException extends ServiceException{
    public CaStateException() {
        super();
    }

    public CaStateException(String message) {
        super(message);
    }

    public CaStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaStateException(Throwable cause) {
        super(cause);
    }

    protected CaStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
