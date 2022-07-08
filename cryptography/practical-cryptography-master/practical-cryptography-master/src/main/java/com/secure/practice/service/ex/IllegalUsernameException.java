package com.secure.practice.service.ex;

public class IllegalUsernameException extends ServiceException{
    public IllegalUsernameException() {
    }

    public IllegalUsernameException(String message) {
        super(message);
    }

    public IllegalUsernameException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalUsernameException(Throwable cause) {
        super(cause);
    }

    public IllegalUsernameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
