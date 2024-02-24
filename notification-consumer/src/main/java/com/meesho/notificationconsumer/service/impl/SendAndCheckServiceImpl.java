package com.meesho.notificationconsumer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.notificationconsumer.entity.redis.BlackListCache;
import com.meesho.notificationconsumer.entity.sql.Notify;
import com.meesho.notificationconsumer.entity.elasticsearch.Sms;
import com.meesho.notificationconsumer.exception.ResourceNotFoundException;
import com.meesho.notificationconsumer.payload.NotifyDto;
import com.meesho.notificationconsumer.repository.BlackListCacheRepo;
import com.meesho.notificationconsumer.repository.NotifyRepo;
import com.meesho.notificationconsumer.repository.SmsRepo;
import com.meesho.notificationconsumer.service.IMIConnectService;
import com.meesho.notificationconsumer.service.SendAndCheckService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class SendAndCheckServiceImpl implements SendAndCheckService {
    private final ObjectMapper objectMapper;
    private ModelMapper modelMapper;
    private NotifyRepo notifyRepo;
    private BlackListCacheRepo blackListCacheRepo;

    private IMIConnectService iMIConnectService;

    private SmsRepo smsRepo;

    @Autowired
    public SendAndCheckServiceImpl(ObjectMapper objectMapper, ModelMapper modelMapper, NotifyRepo notifyRepo, BlackListCacheRepo blackListCacheRepo, IMIConnectService iMIConnectService, SmsRepo smsRepo) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.notifyRepo = notifyRepo;
        this.blackListCacheRepo = blackListCacheRepo;
        this.iMIConnectService = iMIConnectService;
        this.smsRepo = smsRepo;
    }

    public SendAndCheckServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void updateAndValidate(NotifyDto receivednotifyDto) {
        Notify notify = notifyRepo.findById(receivednotifyDto.getId()).orElseThrow(() -> new ResourceNotFoundException("NOT_FOUND", "Resource not found "));
        LocalDateTime currentDateTime = LocalDateTime.now();
        notify.setUpdatedAt(currentDateTime);
        notify.setStatus("received");
        log.info("Message received {}", receivednotifyDto.toString());
        notifyRepo.save(notify);

        Sms sms = Sms.builder().id(notify.getId()).message(notify.getMessage()).phoneNumber(notify.getPhoneNumber()).updatedAt(notify.getUpdatedAt()).createdAt(notify.getCreatedAt()).build();
        smsRepo.save(sms);

        Optional<BlackListCache> blackListCache = Optional.ofNullable(blackListCacheRepo.findBlackListCacheByPhoneNumber(receivednotifyDto.getPhoneNumber()).orElse(null));
        if (blackListCache.isPresent()) {
            log.info("the number {} is blacklisted so cannot send the message", blackListCache.get().getPhoneNumber());
            return;
        }
        iMIConnectService.sendNotification(notify.getPhoneNumber(), notify.getMessage(), notify.getId());
        log.info("Message sent successfully to the number {}", notify.getPhoneNumber());

    }
}
