package com.meesho.notificationserver.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProducerServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private ProducerService producerService;

    @Test
    public void testSendMessage_Success() throws ExecutionException, InterruptedException {
        // Arrange
        String payload = "testPayload";
        String topic = "testTopic";
        ProducerRecord<String, String> expectedProducerRecord = new ProducerRecord<>(topic, payload);
        CompletableFuture<SendResult<String, String>> completableFuture = new CompletableFuture<>();
        completableFuture.complete(mock(SendResult.class));
        when(kafkaTemplate.send(Mockito.any(ProducerRecord.class))).thenReturn(completableFuture);
        producerService.SendMessage(payload, topic);
        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(expectedProducerRecord);
    }

    @Test
    public void testSendMessage_KafkaException() throws ExecutionException, InterruptedException {
        // Arrange
        String payload = "testPayload";
        String topic = "testTopic";
        when(kafkaTemplate.send(Mockito.any(ProducerRecord.class))).thenThrow(new KafkaException("Simulated KafkaException"));
        producerService.SendMessage(payload, topic);
        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(Mockito.any(ProducerRecord.class));

    }

    @Test
    public void testSendMessage_Exception() throws ExecutionException, InterruptedException {
        // Arrange
        String payload = "testPayload";
        String topic = "testTopic";
        when(kafkaTemplate.send(Mockito.any(ProducerRecord.class))).thenThrow(new RuntimeException("Simulated RuntimeException"));
        producerService.SendMessage(payload, topic);
        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(Mockito.any(ProducerRecord.class));
    }
}