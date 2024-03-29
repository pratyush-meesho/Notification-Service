package com.meesho.notificationserver.controller;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import com.meesho.notificationserver.entity.elasticsearch.Sms;
import com.meesho.notificationserver.exception.InvalidRequestException;
import com.meesho.notificationserver.service.ElasticService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/find")
public class ElasticSearchController {
    private ElasticService elasticService;

    ElasticSearchController(ElasticService elasticService) {
        this.elasticService = elasticService;
    }

    private final Logger logger = LoggerFactory.getLogger(ElasticSearchController.class);

    @GetMapping("")
    public ResponseEntity<List<Sms>> findAllValues(@RequestParam(value = "from") LocalDateTime from, @RequestParam(value = "to") LocalDateTime to) {
        List<Sms> sms_list = new ArrayList<>();
        try {
            sms_list = elasticService.findByStartDateAndEndDateForCreation(from, to);
            logger.info("Find all value from start date: {} to end date: {}", from, to);
        } catch (ElasticsearchException elx) {
            logger.error("Elastic Search Exception occurred , StackTrace {}", ExceptionUtils.getStackTrace(elx));
        } catch (IllegalArgumentException ex) {
            logger.error("Illegal Argument Exception has occurred, with the message: {}, StackTrace {}", ex.getMessage(), ExceptionUtils.getStackTrace(ex));

        } catch (InvalidRequestException bex) {
            logger.error("Invalid Request Exception occurred with message {}, StackTrace {}", bex.getMessage(), ExceptionUtils.getStackTrace(bex));
        } catch (Exception ex) {
            logger.info("Internal Server Error occurred, StackTrace {}", ExceptionUtils.getStackTrace(ex));
        }
        return new ResponseEntity<>(sms_list, HttpStatus.ACCEPTED);
    }

    @GetMapping("/message")
    public ResponseEntity<List<Sms>> findAllSmsHaving(@RequestParam("ms") String ms) {
        List<Sms> sms_list = elasticService.findByStringContaining(ms);
        try {
            sms_list = elasticService.findByStringContaining(ms);
            logger.info("Returning messages has the given string {}", ms);
        } catch (ElasticsearchException elx) {
            logger.error("Elastic Search Exception occurred , StackTrace {}", ExceptionUtils.getStackTrace(elx));
        } catch (InvalidRequestException bex) {
            logger.error("Invalid Request Exception occurred with message {}, StackTrace {}", bex.getMessage(), ExceptionUtils.getStackTrace(bex));
        } catch (Exception ex) {
            logger.info("Internal Server Error occurred, StackTrace {}", ExceptionUtils.getStackTrace(ex));
        }
        return new ResponseEntity<>(sms_list, HttpStatus.ACCEPTED);
    }

}
