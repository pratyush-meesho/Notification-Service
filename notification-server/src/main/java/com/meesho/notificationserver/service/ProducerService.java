package com.meesho.notificationserver.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    ProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String payload, String topic) {
        try {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, payload);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);
            log.info(producerRecord.toString());
            log.info("message successfully sent to the topic");
        } catch (KafkaException kex) {
            log.error("Unable to send message to kafka with Stack trace: {}", ExceptionUtils.getStackTrace(kex));
            return;
        } catch (Exception ex) {
            log.error("An unexpected error occurred while sending the message {} with Stack trace {}", ex.getMessage(), ExceptionUtils.getStackTrace(ex));
            return;
        }
    }
}
