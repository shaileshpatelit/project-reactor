package com.shailesh.reactorcommon.rest.exceptionhandler;

import com.shailesh.reactorcommon.exception.ServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Handle
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {ServiceException.class})
    public ResponseEntity<Object> resourceNotFoundException(ServiceException ex, ServerHttpRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", ex.getHttpStatus().value());
        response.put("error", ex.getResponseBody());
        response.put("path", request.getPath().value());
        return new ResponseEntity<Object>(response, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
}