package com.meesho.notificationserver.service;

import com.meesho.notificationserver.entity.Sms;
import com.meesho.notificationserver.exception.CustomTimeoutException;
import com.meesho.notificationserver.repository.SmsRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ElasticServiceTest {

    @Mock
    private SmsRepo smsRepo;

    @Value("${api.timeout.time}")
    private Integer timeout;

    @InjectMocks
    private ElasticService elasticService;
    @Test
    public void testFindByStartDateAndEndDateForCreation() throws Exception {
        // Arrange
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusHours(1);
        Sms sms = Sms.builder().id(UUID.randomUUID()).createdAt(startDateTime).message("hi").phoneNumber("1234").build();
        Sms sms2 = Sms.builder().id(UUID.randomUUID()).createdAt(startDateTime).message("hi").phoneNumber("1234").build();
        List<Sms> expectedSmsList = new ArrayList<>();
        expectedSmsList.add(sms);
        expectedSmsList.add(sms2);
        when(smsRepo.findByCreatedAtBetween(startDateTime, endDateTime)).thenReturn(expectedSmsList);
        List<Sms> result = elasticService.findByStartDateAndEndDateForCreation(startDateTime, endDateTime);
        assertEquals(expectedSmsList, result);
    }

    @Test
    public void testFindByStartDateAndEndDateForUpdate() throws Exception {
        // Arrange
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        Sms sms = Sms.builder().id(UUID.randomUUID()).updatedAt(startDateTime).message("hi").phoneNumber("1234").build();
        Sms sms2 = Sms.builder().id(UUID.randomUUID()).updatedAt(startDateTime).message("hi2").phoneNumber("1234").build();
        List<Sms> expectedSmsList = new ArrayList<>();
        expectedSmsList.add(sms);
        expectedSmsList.add(sms2);
        when(smsRepo.findByUpdatedAtBetween(startDateTime, endDateTime)).thenReturn(expectedSmsList);
        List<Sms> result = elasticService.findByStartDateAndEndDateForUpdate(startDateTime, endDateTime);
        assertEquals(expectedSmsList, result);

    }

    @Test
    public void testFindByStringContaining() throws Exception {
        // Arrange
        String smallMessage = "hi";
        Sms sms = Sms.builder().id(UUID.randomUUID()).message("hi").phoneNumber("1234").build();
        Sms sms2 = Sms.builder().id(UUID.randomUUID()).message("hi2").phoneNumber("1234").build();
        List<Sms> expectedSmsList = new ArrayList<>();
        expectedSmsList.add(sms);
        expectedSmsList.add(sms2);
        when(smsRepo.findByMessageContainingIgnoreCase(smallMessage)).thenReturn(expectedSmsList);
        List<Sms> result = elasticService.findByStringContaining(smallMessage);
        assertEquals(expectedSmsList, result);
    }
}