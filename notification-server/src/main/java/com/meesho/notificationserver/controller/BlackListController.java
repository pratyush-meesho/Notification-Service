package com.meesho.notificationserver.controller;

import com.meesho.notificationserver.exception.InvalidRequestException;
import com.meesho.notificationserver.exception.PhoneNumberInvalidException;
import com.meesho.notificationserver.payload.dto.PhoneNumberListDto;
import com.meesho.notificationserver.service.impl.BlackListServiceImpl;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/blacklist")
public class BlackListController {

    BlackListServiceImpl blackListServiceImpl;
    private final Logger logger = LoggerFactory.getLogger(BlackListController.class);

    public BlackListController(BlackListServiceImpl blackListServiceImpl) {
        this.blackListServiceImpl = blackListServiceImpl;
    }

    @PostMapping("")
    public ResponseEntity<String> addNum(@RequestBody PhoneNumberListDto phoneNumberListDto) {
        try {
            logger.info("phone number list accepted");
            blackListServiceImpl.addPhoneNumberList(phoneNumberListDto.getPhoneNumbers());
            return new ResponseEntity<>("Successfully BlackListed", HttpStatus.CREATED);
        } catch (InvalidRequestException bex) {
            logger.error("InvalidRequestException occurred, StackTrace: {}", ExceptionUtils.getStackTrace(bex));
            return new ResponseEntity<>(bex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (PhoneNumberInvalidException pex) {
            logger.error("Phone Number Invalid, StackTrace: {}", ExceptionUtils.getStackTrace(pex));
            return new ResponseEntity<>(pex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @DeleteMapping("/del")
    public ResponseEntity<String> delNum(@RequestBody PhoneNumberListDto phoneNumberListDto) {
        try {
            blackListServiceImpl.deletePhoneNumberList(phoneNumberListDto.getPhoneNumbers());
            return new ResponseEntity<>("Successfully WhiteListed", HttpStatus.CREATED);
        } catch (InvalidRequestException bex) {
            logger.error("InvalidRequestException occurred, StackTrace: {}", ExceptionUtils.getStackTrace(bex));
            return new ResponseEntity<>(bex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (PhoneNumberInvalidException pex) {
            logger.error("Phone Number Invalid, StackTrace: {}", ExceptionUtils.getStackTrace(pex));
            return new ResponseEntity<>(pex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
