package com.meesho.notificationserver.exception;
import com.meesho.notificationserver.payload.ErrorResponse;
import com.meesho.notificationserver.payload.ErrorResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
////
@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(InvalidRequestException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException in){
//        ErrorResponseData errorResponseData =new ErrorResponseData(in.getMessage(),in.getCode());
//        ErrorResponse errorResponse = new ErrorResponse(errorResponseData);
//        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
//    }
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
//        ErrorResponseData errorData = new ErrorResponseData();
//        errorData.setCode("INTERNAL_SERVER_ERROR");
//        errorData.setMessage("An unexpected error oc");
//        ErrorResponse errorResponse = new ErrorResponse(errorData);
//        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
