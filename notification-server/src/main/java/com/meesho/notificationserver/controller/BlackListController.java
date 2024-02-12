package com.meesho.notificationserver.controller;

import com.meesho.notificationserver.entity.BlackList;
import com.meesho.notificationserver.payload.ApiResponse;
import com.meesho.notificationserver.payload.ApiResponseData;
import com.meesho.notificationserver.payload.PhoneNumberList;
import com.meesho.notificationserver.service.BlackListService;
import jakarta.ws.rs.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/blacklist")
public class BlackListController {
    @Autowired
    BlackListService blackListService;
    private Logger logger = LoggerFactory.getLogger(BlackListController.class);
    @PostMapping("")
    public ResponseEntity<String>addNum(@RequestBody PhoneNumberList phoneNumberList){
        try {
            logger.info("[POST]","phone number list accepted",BlackListController.class);
            blackListService.AddPhoneNumberList(phoneNumberList.getPhoneNumbers());
            return new ResponseEntity<>("Successfully BlackListed", HttpStatus.CREATED);
        }
        catch(BadRequestException bex){
            logger.error("[POST]","got bad request",BlackListController.class);
            ApiResponseData apiResponseData = new ApiResponseData("ERROR","Received bad request");
            ApiResponse apiResponse = new ApiResponse(apiResponseData);
            return new ResponseEntity<>("Got bad request",HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/del")
    public ResponseEntity<String>DelNum(@RequestBody PhoneNumberList phoneNumberList){
        blackListService.DeletePhoneNumberList(phoneNumberList.getPhoneNumbers());
        return new ResponseEntity<>("Successfully WhiteListed", HttpStatus.CREATED);
    }


}
