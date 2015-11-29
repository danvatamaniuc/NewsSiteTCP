package com.vda.gecko.data.exceptions;

/**
 * Created by 1 on 11/28/2015.
 */
public class ServiceException extends RuntimeException {
    public ServiceException(Exception e) {
        super(e);
    }

    public ServiceException(String message) {
        super(message);
    }
}
