package com.meesho.notificationserver.controller;

import com.meesho.notificationserver.payload.ApiResponse;
import com.meesho.notificationserver.payload.ApiResponseData;
import com.meesho.notificationserver.payload.NotifyDto;
import com.meesho.notificationserver.service.ProducerService;
import com.meesho.notificationserver.service.NotifyService;
import jakarta.ws.rs.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/v1/sms")
public class NotifyController {
    @Autowired
    NotifyService notifyService;
    @Autowired
    ProducerService producerService;
    private Logger logger = LoggerFactory.getLogger(NotifyController.class);
    public NotifyController(ProducerService producerService){
        this.producerService = producerService;
    }
    @PostMapping("/send")
    public ResponseEntity<ApiResponse>sendNotification(@RequestBody NotifyDto notifyDto){
        try {
            logger.info("[POST]", "notify dto accepted", NotifyController.class);
            NotifyDto savedNotifyDto = notifyService.saveNotification(notifyDto);
            producerService.SendNotification(savedNotifyDto);
            ApiResponseData apiResponseData = new ApiResponseData(savedNotifyDto.getPhoneNumber(), "Successfully message created and sent");
            ApiResponse apiResponse = new ApiResponse(apiResponseData);
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }
        catch( BadRequestException ex){
            logger.error("[POST]","got bad request",NotifyController.class);
            ApiResponseData apiResponseData = new ApiResponseData("ERROR","Received bad request");
            ApiResponse apiResponse = new ApiResponse(apiResponseData);
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);

        }
    }
    @DeleteMapping("/{requestId}")
    public ResponseEntity<ApiResponse>deleteNotification(@PathVariable("requestId")UUID requestId){
        logger.info("[DELETE]","request Id accepted",NotifyController.class);
        notifyService.deleteNotification(requestId);
        ApiResponseData apiResponseData = new ApiResponseData(requestId.toString(),"Successfully Deleted");
        ApiResponse apiResponse = new ApiResponse(apiResponseData);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }



}
