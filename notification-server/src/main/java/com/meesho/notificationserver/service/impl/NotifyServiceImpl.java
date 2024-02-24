package com.meesho.notificationserver.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.notificationserver.config.AppConstants;
import com.meesho.notificationserver.exception.InvalidRequestException;
import com.meesho.notificationserver.exception.PhoneNumberInvalidException;
import com.meesho.notificationserver.exception.ResourceNotFoundException;
import com.meesho.notificationserver.payload.dto.NotifyDto;
import com.meesho.notificationserver.entity.sql.Notify;
import com.meesho.notificationserver.repository.NotifyRepo;
import com.meesho.notificationserver.service.NotifyService;
import com.meesho.notificationserver.service.ProducerService;
import com.meesho.notificationserver.utils.CheckHandler;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class NotifyServiceImpl implements NotifyService {
    private final NotifyRepo notifyRepo;
    private final ModelMapper modelMapper;
    private final ProducerService producerService;
    private final ObjectMapper objectMapper;

    public NotifyServiceImpl(NotifyRepo notifyRepo, ModelMapper modelMapper, ObjectMapper objectMapper, ProducerService producerService) {
        this.notifyRepo = notifyRepo;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.producerService = producerService;
    }

    public NotifyDto saveNotification(NotifyDto notifyDto) throws JsonProcessingException, PhoneNumberInvalidException {
        if (notifyDto.getPhoneNumber() == null || notifyDto.getPhoneNumber().isEmpty() || notifyDto.getPhoneNumber().isBlank()) {
            throw new InvalidRequestException("INVALID_REQUEST", "Phone number is mandatory");
        }
        if (notifyDto.getMessage() == null) {
            throw new InvalidRequestException("INVALID_REQUEST", "Message is null");
        }

        CheckHandler.phoneNumCheckValid(notifyDto.getPhoneNumber());
        Notify notify = this.modelMapper.map(notifyDto, Notify.class);
        LocalDateTime currentDateTime = LocalDateTime.now();
        notify.setCreatedAt(currentDateTime);
        notify.setStatus("sent");
        Notify savedNotify = notifyRepo.save(notify);
        NotifyDto savednotifyDto = NotifyDto.builder().
                id(savedNotify.getId())
                .message(savedNotify.getMessage())
                .phoneNumber(savedNotify.getPhoneNumber())
                .createdAt(savedNotify.getCreatedAt()).build();
        String payload = objectMapper.writeValueAsString(savednotifyDto);
        log.info(savedNotify.toString(), savednotifyDto.toString());
        producerService.sendMessage(payload, AppConstants.TOPIC_NAME);
        log.info("notification saved in db for the phone number {} with the unique id {}", notify.getPhoneNumber(), notify.getId());
        return savednotifyDto;

    }

    public void deleteNotification(UUID requestID) {
        if (requestID == null) {
            throw new InvalidRequestException("INVALID_REQUEST", "RequestID is mandatory");
        }
        Notify notify = this.notifyRepo.findById(requestID).orElseThrow(() -> new ResourceNotFoundException("NOT_FOUND", "The requested Id to be deleted no found"));
        notifyRepo.delete(notify);
    }


}
