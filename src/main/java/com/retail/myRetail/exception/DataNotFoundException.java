package com.retail.myRetail.exception;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException() {
        super();
    }

    public DataNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
