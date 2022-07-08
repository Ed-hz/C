package com.secure.practice.service.ex;

public class CaTypeException extends ServiceException{
    public CaTypeException() {
        super();
    }

    public CaTypeException(String message) {
        super(message);
    }

    public CaTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaTypeException(Throwable cause) {
        super(cause);
    }

    protected CaTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
