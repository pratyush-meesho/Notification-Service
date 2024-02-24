package com.meesho.notificationserver.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidRequestException extends RuntimeException {
    private final String code;

    public InvalidRequestException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
