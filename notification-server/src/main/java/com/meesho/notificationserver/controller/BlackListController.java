package com.meesho.notificationserver.controller;
import com.meesho.notificationserver.payload.NotifyApiResponse;
import com.meesho.notificationserver.payload.NotifyApiResponseData;
import com.meesho.notificationserver.payload.PhoneNumberList;
import com.meesho.notificationserver.service.BlackListService;
import jakarta.ws.rs.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/blacklist")
public class BlackListController {

    BlackListService blackListService;
    private final Logger logger = LoggerFactory.getLogger(BlackListController.class);
    public BlackListController(BlackListService blackListService){
        this.blackListService = blackListService;
    }
    @PostMapping("")
    public ResponseEntity<String>addNum(@RequestBody PhoneNumberList phoneNumberList){
        try {
            logger.info("phone number list accepted");
            blackListService.AddPhoneNumberList(phoneNumberList.getPhoneNumbers());
            return new ResponseEntity<>("Successfully BlackListed", HttpStatus.CREATED);
        }
        catch(BadRequestException bex){
            logger.error("got bad request");
            NotifyApiResponseData apiResponseData = new NotifyApiResponseData("ERROR","Received bad request");
            NotifyApiResponse notifyApiResponse = new NotifyApiResponse(apiResponseData);
            return new ResponseEntity<>("Got bad request",HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/del")
    public ResponseEntity<String>DelNum(@RequestBody PhoneNumberList phoneNumberList){
        try {
            blackListService.DeletePhoneNumberList(phoneNumberList.getPhoneNumbers());
            return new ResponseEntity<>("Successfully WhiteListed", HttpStatus.CREATED);
        }
        catch(BadRequestException bex){
            logger.error("got bad request");
            NotifyApiResponseData apiResponseData = new NotifyApiResponseData("ERROR","Received bad request");
            NotifyApiResponse notifyApiResponse = new NotifyApiResponse(apiResponseData);
            return new ResponseEntity<>("Got bad request",HttpStatus.BAD_REQUEST);
        }
    }


}
