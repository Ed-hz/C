package com.secure.practice.service.ex;

public class UserTypeException extends ServiceException{
    public UserTypeException() {
        super();
    }

    public UserTypeException(String message) {
        super(message);
    }

    public UserTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserTypeException(Throwable cause) {
        super(cause);
    }

    protected UserTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
