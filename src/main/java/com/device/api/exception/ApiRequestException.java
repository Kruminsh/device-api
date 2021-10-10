package com.device.api.exception;

import org.springframework.http.HttpStatus;

public class ApiRequestException extends RuntimeException {

    private static final HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private final HttpStatus httpStatus;

    public ApiRequestException(String message) {
        super(message);
        this.httpStatus = DEFAULT_HTTP_STATUS;
    }
    public ApiRequestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
