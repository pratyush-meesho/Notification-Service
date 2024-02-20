package com.meesho.notificationserver.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.notificationserver.config.AppConstants;
import com.meesho.notificationserver.entity.Notify;
import com.meesho.notificationserver.exception.InvalidRequestException;
import com.meesho.notificationserver.exception.ResourceNotFoundException;
import com.meesho.notificationserver.payload.NotifyDto;
import com.meesho.notificationserver.repository.NotifyRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotifyServiceTest {
    @Mock
    private NotifyRepo notifyRepo;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ProducerService producerService;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private  NotifyService notifyService;
    private NotifyDto notifyDto;
    private Notify notifyEntity;
    @BeforeEach
    public void setUp() {
         notifyDto = NotifyDto.builder().Id(UUID.randomUUID())
                .phoneNumber("1234567890")
                .message("Test message")
                .build();
         notifyEntity= new Notify();
        notifyEntity.setId(notifyDto.getId());
        notifyEntity.setPhoneNumber(notifyDto.getPhoneNumber());
        notifyEntity.setMessage(notifyDto.getMessage());
        notifyEntity.setCreatedAt(LocalDateTime.now());
        notifyEntity.setStatus("sent");
    }
    @Test
    public void saveNotification_Success() throws JsonProcessingException {

        //Mockito.when(modelMapper.map(Mockito.eq(NotifyDto.class), Mockito.eq(Notify.class))).thenReturn(Mockito.any(Notify.class));
        Mockito.when(modelMapper.map(Mockito.any(NotifyDto.class), Mockito.eq(Notify.class))).thenReturn(notifyEntity);
        Mockito.when(notifyRepo.save(Mockito.any(Notify.class))).thenReturn(notifyEntity);
        //Mockito.when(objectMapper.writeValueAsString(Mockito.any(NotifyDto.class))).thenReturn("sample payload");
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(NotifyDto.class))).thenReturn("Sample payload");
        NotifyDto result = notifyService.saveNotification(notifyDto);
        assertEquals(result.getId(),notifyDto.getId());
        assertEquals(result.getStatus(),notifyDto.getStatus());
        Mockito.verify(producerService, Mockito.times(1)).SendMessage(Mockito.anyString(), Mockito.anyString());
    }
    @Test
    public void testSaveNotification_emptyPhoneNumber() {
        NotifyDto invalidDto = NotifyDto.builder().phoneNumber("").Id(UUID.randomUUID()).createdAt(LocalDateTime.now()).message("message").build();
        // Assert throws with appropriate message
        assertThrows(InvalidRequestException.class, () -> notifyService.saveNotification(invalidDto));
    }
    @Test
    public void testSaveNotification_NullPhoneNumber() {
        NotifyDto invalidDto = NotifyDto.builder().createdAt(LocalDateTime.now()).Id(UUID.randomUUID()).message("message").build();
        // Assert throws with appropriate message
        assertThrows(InvalidRequestException.class, () -> notifyService.saveNotification(invalidDto));
    }

    @Test
    public void testDeleteNotification_success(){
        UUID requestId = UUID.randomUUID();
        Notify notify = new Notify();
        notify.setId(requestId);

        // Mocking the behavior of repository
        when(notifyRepo.findById(requestId)).thenReturn(Optional.of(notify));

        // Act
        notifyService.deleteNotification(requestId);

        // Assert
        verify(notifyRepo, times(1)).delete(notify);


    }
    @Test
    public void testDeleteNotification_NotFound(){
        UUID requestId = UUID.randomUUID();

        // Mocking the behavior of repository
        when(notifyRepo.findById(requestId)).thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> notifyService.deleteNotification(requestId));

        assertEquals("NOT_FOUND", exception.getCode());
        assertEquals("The requested Id to be deleted no found", exception.getMessage());
        verify(notifyRepo, never()).delete(any());

    }


}