package com.meesho.notificationserver.controller;

import com.meesho.notificationserver.entity.Sms;
import com.meesho.notificationserver.service.ElasticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
//@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
@RestController
@RequestMapping("/api/v1/find")
public class MonitorController {
    @Autowired
    ElasticService elasticService;
    private Logger logger = LoggerFactory.getLogger(MonitorController.class);
    @GetMapping("")
    public ResponseEntity<?> findAllValues(@RequestParam(value = "from") LocalDateTime from, @RequestParam(value = "to")LocalDateTime to){
        logger.info("Find all value from from_date  to to_data ",MonitorController.class);
        List<Sms> sms_list = elasticService.findByStartDateAndEndDateForCreation(from,to);
        return new ResponseEntity<>(sms_list, HttpStatus.ACCEPTED);
    }
    @GetMapping("/message")
    public ResponseEntity<?>findAllSmsHaving(@RequestParam("ms") String ms){
        List<Sms> sms_list = elasticService.findByStringContaining(ms);
        logger.info("Searching based on small msg");
        return new ResponseEntity<>(sms_list,HttpStatus.ACCEPTED);
    }
}
