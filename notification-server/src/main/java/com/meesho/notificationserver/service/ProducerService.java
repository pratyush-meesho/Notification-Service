package com.meesho.notificationserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.notificationserver.config.AppConstants;
import com.meesho.notificationserver.payload.NotifyDto;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.glassfish.jersey.internal.guava.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

@Service
public class ProducerService {


    private Logger logger = LoggerFactory.getLogger(ProducerService.class);

    private KafkaTemplate<String,String> kafkaTemplate;
    ProducerService(KafkaTemplate<String,String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    public void SendMessage(String payload,String topic){
        try {
            //Message<NotifyDto> message = MessageBuilder.withPayload(notifyDto).setHeader(KafkaHeaders.TOPIC, AppConstants.TOPIC_NAME).build()
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, payload);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);
            logger.info(producerRecord.toString());
            logger.info("message successfully sent to the topic");
        }
        catch(KafkaException kex){
            logger.error("Unable to send message to kafka with Stack trace: {}", ExceptionUtils.getStackTrace(kex));
            return;
        }
        catch (Exception ex) {
            logger.error("An unexpected error occurred while sending the message {} with Stack trace {}", ex.getMessage(), ExceptionUtils.getStackTrace(ex));
            return;
        }
    }
}
