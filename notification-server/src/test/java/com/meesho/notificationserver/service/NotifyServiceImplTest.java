package com.meesho.notificationserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.notificationserver.entity.sql.Notify;
import com.meesho.notificationserver.exception.InvalidRequestException;
import com.meesho.notificationserver.exception.PhoneNumberInvalidException;
import com.meesho.notificationserver.exception.ResourceNotFoundException;
import com.meesho.notificationserver.payload.dto.NotifyDto;
import com.meesho.notificationserver.repository.NotifyRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotifyServiceImplTest {
    @Mock
    private NotifyRepo notifyRepo;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ProducerService producerService;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private NotifyService notifyService;
    private NotifyDto notifyDto;
    private Notify notifyEntity;
    @Test
    public void saveNotification_Success() throws JsonProcessingException, PhoneNumberInvalidException {

        //Mockito.when(modelMapper.map(Mockito.eq(NotifyDto.class), Mockito.eq(Notify.class))).thenReturn(Mockito.any(Notify.class));
        Mockito.when(modelMapper.map(Mockito.any(NotifyDto.class), Mockito.eq(Notify.class))).thenReturn(notifyEntity);
        Mockito.when(notifyRepo.save(Mockito.any(Notify.class))).thenReturn(notifyEntity);
        //Mockito.when(objectMapper.writeValueAsString(Mockito.any(NotifyDto.class))).thenReturn("sample payload");
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(NotifyDto.class))).thenReturn("Sample payload");
        NotifyDto result = notifyService.saveNotification(notifyDto);
        assertEquals(result.getId(), notifyDto.getId());
        assertEquals(result.getStatus(), notifyDto.getStatus());
        Mockito.verify(producerService, Mockito.times(1)).sendMessage(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testSaveNotificationEmptyPhoneNumber() {
        NotifyDto invalidDto = NotifyDto.builder().phoneNumber("").id(UUID.randomUUID()).createdAt(LocalDateTime.now()).message("message").build();
        // Assert throws with appropriate message
        assertThrows(InvalidRequestException.class, () -> notifyService.saveNotification(invalidDto));
    }

    @Test
    public void testSaveNotificationNullPhoneNumber() {
        NotifyDto invalidDto = NotifyDto.builder().createdAt(LocalDateTime.now()).id(UUID.randomUUID()).message("message").build();
        // Assert throws with appropriate message
        assertThrows(InvalidRequestException.class, () -> notifyService.saveNotification(invalidDto));
    }

    @Test
    public void testSaveNotificationInValidPhoneNUmber() {
        NotifyDto invalidDto = NotifyDto.builder().createdAt(LocalDateTime.now()).id(UUID.randomUUID()).message("message").phoneNumber("123456").build();
        assertThrows(PhoneNumberInvalidException.class,()->notifyService.saveNotification(invalidDto));

    }


    @Test

    public void testDeleteNotification_success() {
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
    public void testDeleteNotification_NotFound() {
        UUID requestId = UUID.randomUUID();

        // Mocking the behavior of repository
        when(notifyRepo.findById(requestId)).thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> notifyService.deleteNotification(requestId));

        assertEquals("NOT_FOUND", exception.getCode());
        assertEquals("The requested Id to be deleted not found", exception.getMessage());
        verify(notifyRepo, never()).delete(any());

    }


}