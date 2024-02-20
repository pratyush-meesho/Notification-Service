package com.meesho.notificationserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.notificationserver.config.AppConstants;
import com.meesho.notificationserver.exception.InvalidRequestException;
import com.meesho.notificationserver.exception.ResourceNotFoundException;
import com.meesho.notificationserver.payload.NotifyDto;
import com.meesho.notificationserver.entity.Notify;
import com.meesho.notificationserver.repository.NotifyRepo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NotifyService {
    private final NotifyRepo notifyRepo;
    private  final ModelMapper modelMapper;
    private  final ProducerService producerService;
    private  final ObjectMapper objectMapper;
    public NotifyService(NotifyRepo notifyRepo, ModelMapper modelMapper, ObjectMapper objectMapper,ProducerService producerService){
        this.notifyRepo = notifyRepo;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.producerService = producerService;
    }
    private Logger logger = LoggerFactory.getLogger(NotifyService.class);
    public NotifyDto saveNotification (NotifyDto notifyDto) throws JsonProcessingException {
        if( notifyDto.getPhoneNumber()==null|| notifyDto.getPhoneNumber().isEmpty() || notifyDto.getPhoneNumber().isBlank()){
            throw new InvalidRequestException("INVALID_REQUEST","Phone number is mandatory");
        }
            Notify notify = this.modelMapper.map(notifyDto, Notify.class);
            LocalDateTime currentDateTime = LocalDateTime.now();
            notify.setCreatedAt(currentDateTime);
            notify.setStatus("sent");
            Notify savedNotify = notifyRepo.save(notify);
//            convert to DTO
        //NotifyDto savednotifyDto = this.modelMapper.map(savedNotify, NotifyDto.class);
        NotifyDto savednotifyDto = NotifyDto.builder().Id(savedNotify.getId()).message(savedNotify.getMessage()).phoneNumber(savedNotify.getPhoneNumber()).createdAt(savedNotify.getCreatedAt()).build();
        String payload = objectMapper.writeValueAsString(savednotifyDto);
        logger.info(savedNotify.toString(),savednotifyDto.toString());
        producerService.SendMessage(payload, AppConstants.TOPIC_NAME);
        logger.info(String.format("notification saved in db for the phone number %s with the unique id %s", notify.getPhoneNumber(), notify.getId()));
        return savednotifyDto;

    }
    public void deleteNotification(UUID requestID){
        Notify notify = this.notifyRepo.findById(requestID).orElseThrow(()->new ResourceNotFoundException("NOT_FOUND","The requested Id to be deleted no found"));
        notifyRepo.delete(notify);
    }



}
