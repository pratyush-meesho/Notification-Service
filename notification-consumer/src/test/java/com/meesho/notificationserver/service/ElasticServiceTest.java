package com.meesho.notificationserver.service;

import com.meesho.notificationserver.entity.Sms;
import com.meesho.notificationserver.repository.SmsRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ElasticServiceTest {
    @Mock
    private SmsRepo smsRepo;

    @InjectMocks
    private ElasticService elasticService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStartDateAndEndDateForCreation() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 2, 0, 0);
        Sms sms1 = new Sms();
        Sms sms2 = new Sms();
        when(smsRepo.findByCreatedAtBetween(startDate, endDate)).thenReturn(Arrays.asList(sms1, sms2));

        List<Sms> result = elasticService.findByStartDateAndEndDateForCreation(startDate, endDate);

        assertEquals(2, result.size());
    }

    @Test
    public void testFindByStartDateAndEndDateForUpdate() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 2, 0, 0);
        Sms sms1 = new Sms();
        Sms sms2 = new Sms();
        when(smsRepo.findByUpdatedAtBetween(startDate, endDate)).thenReturn(Arrays.asList(sms1, sms2));

        List<Sms> result = elasticService.findByStartDateAndEndDateForUpdate(startDate, endDate);

        assertEquals(2, result.size());
    }

    @Test
    public void testFindByStringContaining() {
        String searchString = "test";
        Sms sms1 = new Sms();
        Sms sms2 = new Sms();
        when(smsRepo.findByMessageContainingIgnoreCase(searchString)).thenReturn(Arrays.asList(sms1, sms2));

        List<Sms> result = elasticService.findByStringContaining(searchString);

        assertEquals(2, result.size());
    }


}