package com.meesho.notificationserver.exception;

import com.meesho.notificationserver.payload.ErrorResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.meesho.notificationserver.payload.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse>handleInvalidRequestException(InvalidRequestException in){
        ErrorResponseData errorResponseData =new ErrorResponseData(in.getMessage(),in.getCode());
        ErrorResponse errorResponse = new ErrorResponse(errorResponseData);
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse>handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException){
        ErrorResponseData errorResponseData = new ErrorResponseData(resourceNotFoundException.getMessage(),resourceNotFoundException.getCode());
        ErrorResponse errorResponse  = new ErrorResponse(errorResponseData);
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponseData errorData = new ErrorResponseData();
        errorData.setCode("INTERNAL_SERVER_ERROR");
        errorData.setMessage("An unexpected error occurred");
        ErrorResponse errorResponse = new ErrorResponse(errorData);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
