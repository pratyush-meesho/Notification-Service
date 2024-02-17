package com.meesho.notificationserver.service;

import com.meesho.notificationserver.config.AppConstants;
import com.meesho.notificationserver.payload.NotifyDto;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeoutException;

@Service
public class ProducerService {


    private Logger logger = LoggerFactory.getLogger(ProducerService.class);

    private KafkaTemplate<String,NotifyDto> kafkaTemplate;
    ProducerService(KafkaTemplate<String,NotifyDto> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    public boolean SendNotification(NotifyDto notifyDto){
        try {
            Message<NotifyDto> message = MessageBuilder.withPayload(notifyDto).setHeader(KafkaHeaders.TOPIC, AppConstants.TOPIC_NAME).build();
            kafkaTemplate.send(message);
            logger.info(String.format("message with %s Id sent successfully to the topic",notifyDto.getId()));
        }
        catch(KafkaException kex){
            logger.error("Unable to send message to kafka with Stack trace: {}", ExceptionUtils.getStackTrace(kex));
            return false;
        }
        catch (Exception ex) {
            logger.error("An unexpected error occurred while sending the message {} with Stack trace {}", ex.getMessage(),ExceptionUtils.getStackTrace(ex));
            return false;
        }
        return true;
    }
}
