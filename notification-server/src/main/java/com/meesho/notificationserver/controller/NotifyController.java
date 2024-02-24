package com.meesho.notificationserver.controller;

import com.meesho.notificationserver.exception.InvalidRequestException;
import com.meesho.notificationserver.exception.PhoneNumberInvalidException;
import com.meesho.notificationserver.exception.ResourceNotFoundException;
import com.meesho.notificationserver.payload.dto.NotifyDto;
import com.meesho.notificationserver.payload.response.NotifyApiResponse;
import com.meesho.notificationserver.payload.response.NotifyFailureApiResponse;
import com.meesho.notificationserver.payload.response.NotifySuccessApiResponse;
import com.meesho.notificationserver.service.NotifyService;
import org.slf4j.Logger;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/sms")
public class NotifyController {
    NotifyService notifyService;
    private final Logger logger = LoggerFactory.getLogger(NotifyController.class);

    public NotifyController(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    @PostMapping("/send")
    public ResponseEntity<NotifyApiResponse> sendNotification(@RequestBody NotifyDto notifyDto) {
        try {
            logger.info("[POST] notify dto accepted");
            NotifyDto savedNotifyDto = notifyService.saveNotification(notifyDto);
            NotifySuccessApiResponse.NotifySuccessApiResponseData notifySuccessApiResponseData = NotifySuccessApiResponse.NotifySuccessApiResponseData.builder()
                    .comments("Successfully sent").requestId(savedNotifyDto.getId().toString()).build();
            NotifySuccessApiResponse notifySuccessApiResponse = NotifySuccessApiResponse.builder().notifySuccessApiResponseData(notifySuccessApiResponseData).build();
            return new ResponseEntity<>(notifySuccessApiResponse, HttpStatus.CREATED);
        } catch (InvalidRequestException ex) {
            logger.error("Invalid Request Exception while sending notification, StackTrace : {}", ExceptionUtils.getStackTrace(ex));
            NotifyFailureApiResponse.NotifyFailureApiResponseData notifyFailureApiResponseData = NotifyFailureApiResponse.NotifyFailureApiResponseData.builder()
                    .code("INVALID_REQUEST").message("Received Invalid Request Exception").build();
            NotifyFailureApiResponse notifyFailureApiResponse = NotifyFailureApiResponse.builder().notifyFailureApiResponseData(notifyFailureApiResponseData).build();
            return new ResponseEntity<>(notifyFailureApiResponse, HttpStatus.BAD_REQUEST);
        } catch (PhoneNumberInvalidException ex) {
            logger.error("PhoneNumberInvalidException occurred with message {}, StackTrace: {}", ex.getMessage(), ExceptionUtils.getStackTrace(ex));
            NotifyFailureApiResponse.NotifyFailureApiResponseData notifyFailureApiResponseData = NotifyFailureApiResponse.NotifyFailureApiResponseData.builder()
                    .code("INVALID_REQUEST").message("Invalid Phone Number").build();
            NotifyFailureApiResponse notifyFailureApiResponse = NotifyFailureApiResponse.builder().notifyFailureApiResponseData(notifyFailureApiResponseData).build();
            return new ResponseEntity<>(notifyFailureApiResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            logger.error("Some Internal Server Error occurred, Stack :{}", ExceptionUtils.getStackTrace(ex));
            NotifyFailureApiResponse.NotifyFailureApiResponseData notifyFailureApiResponseData = NotifyFailureApiResponse.NotifyFailureApiResponseData.builder()
                    .code("INTERNAL_SERVER_ERROR").message("Internal server has occurred").build();
            NotifyFailureApiResponse notifyFailureApiResponse = NotifyFailureApiResponse.builder().notifyFailureApiResponseData(notifyFailureApiResponseData).build();
            return new ResponseEntity<>(notifyFailureApiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<NotifyApiResponse> deleteNotification(@PathVariable("requestId") UUID requestId) {
        logger.info("[DELETE] request Id accepted");
        try {
            notifyService.deleteNotification(requestId);
            NotifySuccessApiResponse.NotifySuccessApiResponseData notifySuccessApiResponseData = NotifySuccessApiResponse.NotifySuccessApiResponseData.builder()
                    .comments("Successfully deleted").requestId(requestId.toString()).build();
            NotifySuccessApiResponse notifySuccessApiResponse = NotifySuccessApiResponse.builder().notifySuccessApiResponseData(notifySuccessApiResponseData).build();
            return new ResponseEntity<>(notifySuccessApiResponse, HttpStatus.OK);
        } catch (InvalidRequestException ex) {
            logger.error("Invalid Request Exception while sending notification, StackTrace : {}", ExceptionUtils.getStackTrace(ex));
            NotifyFailureApiResponse.NotifyFailureApiResponseData notifyFailureApiResponseData = NotifyFailureApiResponse.NotifyFailureApiResponseData.builder()
                    .code("INVALID_REQUEST").message("Received Invalid Request Exception").build();
            NotifyFailureApiResponse notifyFailureApiResponse = NotifyFailureApiResponse.builder().notifyFailureApiResponseData(notifyFailureApiResponseData).build();
            return new ResponseEntity<>(notifyFailureApiResponse, HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException rex) {
            logger.error("Resource not found Exception, StackTrace : {}", ExceptionUtils.getStackTrace(rex));
            NotifyFailureApiResponse.NotifyFailureApiResponseData notifyFailureApiResponseData = NotifyFailureApiResponse.NotifyFailureApiResponseData.builder()
                    .code("RESOURCE_NOT_FOUND").message("The Resource not found, so unable to delete").build();
            NotifyFailureApiResponse notifyFailureApiResponse = NotifyFailureApiResponse.builder().notifyFailureApiResponseData(notifyFailureApiResponseData).build();
            return new ResponseEntity<>(notifyFailureApiResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            logger.error("Some Internal Server Error occurred, Stack :{}", ExceptionUtils.getStackTrace(ex));
            NotifyFailureApiResponse.NotifyFailureApiResponseData notifyFailureApiResponseData = NotifyFailureApiResponse.NotifyFailureApiResponseData.builder()
                    .code("INTERNAL_SERVER_ERROR").message("Internal server has occurred").build();
            NotifyFailureApiResponse notifyFailureApiResponse = NotifyFailureApiResponse.builder().notifyFailureApiResponseData(notifyFailureApiResponseData).build();
            return new ResponseEntity<>(notifyFailureApiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


}
