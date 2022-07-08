package com.secure.practice.service.ex;

public class VerifyCodeNotExist extends ControllerException{
    public VerifyCodeNotExist() {
        super();
    }

    public VerifyCodeNotExist(String message) {
        super(message);
    }

    public VerifyCodeNotExist(String message, Throwable cause) {
        super(message, cause);
    }

    public VerifyCodeNotExist(Throwable cause) {
        super(cause);
    }

    protected VerifyCodeNotExist(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
