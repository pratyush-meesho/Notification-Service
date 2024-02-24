//package com.meesho.notificationserver.service;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.meesho.notificationserver.entity.redis.BlackListCache;
//import com.meesho.notificationserver.entity.sql.Notify;
//import com.meesho.notificationserver.payload.NotifyDto;
//import com.meesho.notificationserver.repository.BlackListCacheRepo;
//import com.meesho.notificationserver.repository.NotifyRepo;
//import com.meesho.notificationserver.repository.SmsRepo;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.mockito.Mockito.*;
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//
//@ExtendWith(MockitoExtension.class)
//public class SendAndCheckServiceTest {
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private NotifyRepo notifyRepo;
//
//    @Mock
//    private BlackListCacheRepo blackListCacheRepo;
//
//    @Mock
//    private IMIConnectService iMIConnectService;
//
//    @Mock
//    private SmsRepo smsRepo;
//
//    @InjectMocks
//    private SendAndCheckService sendAndCheckService;
//
//    @Test
//    public void testConsumeWithSuccessfulProcessing() throws Exception {
//        // Arrange
//        ConsumerRecord<String, Object> record = new ConsumerRecord<>("topic", 1, 1, "key", "value");
//        Notify notify = Notify.builder().id(UUID.randomUUID()).message("hi").createdAt(LocalDateTime.now()).phoneNumber("123").build();
//        NotifyDto notifyDto = NotifyDto.builder()
//                .id(notify.getId())
//                .createdAt(notify.getCreatedAt())
//                .message(notify.getMessage())
//                .phoneNumber(notify.getPhoneNumber()).build();
//        when(objectMapper.readValue(anyString(), eq(NotifyDto.class))).thenReturn(notifyDto);
//        when(notifyRepo.findById(notify.getId())).thenReturn(Optional.of(notify));
//        when(blackListCacheRepo.findBlackListCacheByPhoneNumber(anyString())).thenReturn(Optional.empty());
//
//        sendAndCheckService.consume(record);
//
//        verify(notifyRepo, times(1)).save(any(Notify.class));
//        verify(notifyRepo,times(1)).findById(notify.getId());
//        verify(iMIConnectService).sendNotification(anyString(), anyString(), any(UUID.class));
//    }
//
//    @Test
//    public void testConsumeWithEmptyResultDataAccessException() throws Exception {
//        // Arrange
//        ConsumerRecord<String, Object> record = new ConsumerRecord<>("topic", 1, 1, "key", "value");
//        NotifyDto notifyDto = NotifyDto.builder()
//                .id(UUID.randomUUID())
//                .createdAt(LocalDateTime.now())
//                .message("hi")
//                .phoneNumber("1234").build();
//
//        //when(objectMapper.readValue(any(String.class), any(TypeReference.class))).thenReturn(notifyDto);
//        when(objectMapper.readValue(anyString(), eq(NotifyDto.class))).thenReturn(notifyDto);
//        // Act
//        sendAndCheckService.consume(record);
//        // Assert
//        verify(notifyRepo, never()).save(any(Notify.class));
//        verify(iMIConnectService, never()).sendNotification(anyString(), anyString(), any(UUID.class));
//    }
//
//    @Test
//    public void testConsumeWithException() throws Exception {
//        // Arrange
//        ConsumerRecord<String, Object> record = new ConsumerRecord<>("topic", 1, 1, "key", "value");
//        NotifyDto notifyDto = NotifyDto.builder()
//                .id(UUID.randomUUID())
//                .createdAt(LocalDateTime.now())
//                .message("hi")
//                .phoneNumber("1234").build();
//        when(objectMapper.readValue(anyString(), eq(NotifyDto.class))).thenThrow(new RuntimeException("Simulated exception"));
//        sendAndCheckService.consume(record);
//        verify(notifyRepo, never()).save(any(Notify.class));
//        verify(iMIConnectService, never()).sendNotification(anyString(), anyString(), any(UUID.class));
//    }
//
//    @Test
//    public void testConsumeWithBlacklistedNumber() throws Exception {
//        // Arrange
//        ConsumerRecord<String, Object> record = new ConsumerRecord<>("topic", 1, 1, "key", "value");
//        //Notify notify = new Notify(/* add necessary values */);
//        Notify notify = Notify.builder()
//                .id(UUID.randomUUID())
//                .createdAt(LocalDateTime.now())
//                .message("hi")
//                .phoneNumber("1234").build();
//        NotifyDto notifyDto = NotifyDto.builder()
//                .id(UUID.randomUUID())
//                .createdAt(LocalDateTime.now())
//                .message("hi")
//                .phoneNumber("1234").build();
//        when(objectMapper.readValue(any(String.class),eq(NotifyDto.class))).thenReturn(notifyDto);
//        when(notifyRepo.findById(any(UUID.class))).thenReturn(Optional.of(notify));
//        BlackListCache blackListCache = new BlackListCache(notify.getId(), notify.getPhoneNumber());
//        when(blackListCacheRepo.findBlackListCacheByPhoneNumber(anyString())).thenReturn(Optional.of(blackListCache));
//        sendAndCheckService.consume(record);
//        verify(notifyRepo, times(1)).save(any(Notify.class));
//        verify(iMIConnectService, never()).sendNotification(anyString(), anyString(), any(UUID.class));
//    }
//
//    // Add more test cases to cover additional error scenarios and exceptions
//}