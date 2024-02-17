package com.meesho.notificationserver.controller;

import com.meesho.notificationserver.payload.ApiResponse;
import com.meesho.notificationserver.payload.ApiResponseData;
import com.meesho.notificationserver.payload.NotifyDto;
import com.meesho.notificationserver.service.ProducerService;
import com.meesho.notificationserver.service.NotifyService;
import jakarta.ws.rs.BadRequestException;
import org.slf4j.Logger;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/sms")
public class NotifyController {
    NotifyService notifyService;
    ProducerService producerService;
    private final Logger logger = LoggerFactory.getLogger(NotifyController.class);
    public NotifyController(ProducerService producerService,NotifyService notifyService){
        this.notifyService = notifyService;
        this.producerService = producerService;
    }
    @PostMapping("/send")
    public ResponseEntity<Map<String,Object>>sendNotification(@RequestBody NotifyDto notifyDto){
        try {
            logger.info("[POST] notify dto accepted");
            NotifyDto savedNotifyDto = notifyService.saveNotification(notifyDto);
            producerService.SendNotification(savedNotifyDto);
            //ApiResponseData apiResponseData = new ApiResponseData(savedNotifyDto.getPhoneNumber(), "Successfully message created and sent");
            Map<String,Object>resp = new HashMap<>();
            Map<String,Object>Apiresp = new HashMap<>();
            Apiresp.put("requestId",savedNotifyDto.getId());
            Apiresp.put("comments","Successfully sent");
            resp.put("data",Apiresp);
            //ApiResponse apiResponse = new ApiResponse(apiResponseData);
            return new ResponseEntity<>(resp, HttpStatus.CREATED);
        }
        catch( BadRequestException ex){
            logger.error("Bad request Exception while sending notification, StackTrace : {}",ExceptionUtils.getStackTrace(ex));
            ApiResponseData apiResponseData = new ApiResponseData("ERROR","Received bad request");
            ApiResponse apiResponse = new ApiResponse(apiResponseData);
            Map<String,Object>resp = new HashMap<>();
            Map<String,Object>apiResp = new HashMap<>();
            apiResp.put("code","INVALID_REQUEST");
            apiResp.put("message","Recieved bad request");
            resp.put("error",apiResp);
            return new ResponseEntity<>(resp,HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{requestId}")
    public ResponseEntity<Map<String,Object>>deleteNotification(@PathVariable("requestId")UUID requestId){
        logger.info("[DELETE] request Id accepted");
        try {
            notifyService.deleteNotification(requestId);
            Map<String,Object>resp = new HashMap<>();
            Map<String,Object>Apiresp = new HashMap<>();
            Apiresp.put("requestId",requestId);
            Apiresp.put("comments","Successfully deleted");
            resp.put("data",Apiresp);
            //ApiResponse apiResponse = new ApiResponse(apiResponseData);
            //ApiResponseData apiResponseData = new ApiResponseData(requestId.toString(), "Successfully Deleted");
            //ApiResponse apiResponse = new ApiResponse(apiResponseData);
            //return new ResponseEntity<>(apiResponse, HttpStatus.OK);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        catch(BadRequestException ex){
            logger.error("Bad request Exception while sending notification, StackTrace : {}",ExceptionUtils.getStackTrace(ex));
            ApiResponseData apiResponseData = new ApiResponseData("ERROR","Received bad request");
            ApiResponse apiResponse = new ApiResponse(apiResponseData);
            Map<String,Object>resp = new HashMap<>();
            Map<String,Object>apiResp = new HashMap<>();
            apiResp.put("code","INVALID_REQUEST");
            apiResp.put("message","Receieved bad request");
            resp.put("error",apiResp);
            return new ResponseEntity<>(resp,HttpStatus.BAD_REQUEST);

        }
    }



}
