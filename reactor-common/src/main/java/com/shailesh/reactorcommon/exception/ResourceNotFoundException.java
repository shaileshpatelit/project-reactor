package com.shailesh.reactorcommon.exception;


import org.springframework.http.HttpStatus;

/**
 * Not found expception for the service
 */
public class ResourceNotFoundException extends ServiceException {
    public ResourceNotFoundException(String message) {
        super(message);
        super.httpStatus = HttpStatus.NOT_FOUND;
        super.responseBody = message;
    }
}
