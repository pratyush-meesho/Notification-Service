package com.meesho.notificationserver.exception;
import com.meesho.notificationserver.payload.ErrorResponse;
import com.meesho.notificationserver.payload.ErrorResponseData;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse>handleInvalidRequestException(InvalidRequestException in){
        ErrorResponseData errorResponseData =new ErrorResponseData(in.getMessage(),in.getCode());
        ErrorResponse errorResponse = new ErrorResponse(errorResponseData);
        logger.error("Invalid Request Exception , StackTrace {}", ExceptionUtils.getStackTrace(in));
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse>handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException){
        ErrorResponseData errorResponseData = new ErrorResponseData(resourceNotFoundException.getMessage(),resourceNotFoundException.getCode());
        ErrorResponse errorResponse  = new ErrorResponse(errorResponseData);
        logger.error("Resource not found exception , StackTrace {}", ExceptionUtils.getStackTrace(resourceNotFoundException));
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(CustomTimeoutException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public ResponseEntity<ErrorResponse>handleCustomTimeoutException(CustomTimeoutException customTimeoutException){
        ErrorResponseData errorResponseData = new ErrorResponseData(customTimeoutException.getMessage(),"API_TIMEOUT");
        ErrorResponse errorResponse  = new ErrorResponse(errorResponseData);
        logger.error("API has timed out,   StackTrace {}", ExceptionUtils.getStackTrace(customTimeoutException));
        return new ResponseEntity<>(errorResponse,HttpStatus.REQUEST_TIMEOUT);
    }
    @ExceptionHandler(ElasticSearchException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse>handleElasticSearchException(ElasticSearchException ex){
        ErrorResponseData errorResponseData = new ErrorResponseData(ex.getMessage(),"ELASTIC_SEARCH_ERROR");
        ErrorResponse errorResponse  = new ErrorResponse(errorResponseData);
        logger.error("Elastic Search Error occured,   StackTrace {}", ExceptionUtils.getStackTrace(ex));
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
