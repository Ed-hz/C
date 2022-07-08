package com.secure.practice.service.ex;

public class CaNotExistException extends ControllerException{
    public CaNotExistException() {
        super();
    }

    public CaNotExistException(String message) {
        super(message);
    }

    public CaNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaNotExistException(Throwable cause) {
        super(cause);
    }

    protected CaNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
