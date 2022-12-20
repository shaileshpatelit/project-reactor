package com.shailesh.reactorcommon.exception;


import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Not found expception for the service
 */
@Data
public class ServiceException extends Exception {
    HttpStatus httpStatus;
    Object responseBody;

    public ServiceException() {
    }
    public ServiceException(String message) {
        super(message);
    }
    public ServiceException(String message, Throwable t) {
        super(message, t);
    }
}
