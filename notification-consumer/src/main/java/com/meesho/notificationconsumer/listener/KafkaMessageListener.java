package com.meesho.notificationconsumer.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.notificationconsumer.config.AppConstants;
import com.meesho.notificationconsumer.exception.ResourceNotFoundException;
import com.meesho.notificationconsumer.payload.NotifyDto;
import com.meesho.notificationconsumer.service.SendAndCheckService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@Slf4j
public class KafkaMessageListener {
    public ObjectMapper objectMapper;
    public SendAndCheckService sendAndCheckService;

    public KafkaMessageListener(ObjectMapper objectMapper, SendAndCheckService sendAndCheckService) {
        this.objectMapper = objectMapper;
        this.sendAndCheckService = sendAndCheckService;
    }

    @KafkaListener(topics = AppConstants.TOPIC_NAME,
            groupId = AppConstants.GROUP_ID)
    public void listenToKafka(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment) {
        try {
            NotifyDto receivednotifyDto = objectMapper.readValue(record.value().toString(), NotifyDto.class);
            sendAndCheckService.updateAndValidate(receivednotifyDto);
            acknowledgment.acknowledge();

        } catch (JsonProcessingException ex) {
            log.error("Json Processing Exception occurred, StackTrace: {}", ExceptionUtils.getStackTrace(ex));
            acknowledgment.nack(Duration.of(1, ChronoUnit.SECONDS));
        } catch (ResourceNotFoundException rs) {
            log.error("Resource not found occurred, message:{} , StackTrace{}", rs.getMessage(), ExceptionUtils.getStackTrace(rs));
        } catch (Exception e) {
            log.error("Internal Server Error Occurred, StackTrace {}", ExceptionUtils.getStackTrace(e));
            acknowledgment.nack(Duration.of(1, ChronoUnit.SECONDS));
        }


    }
}
