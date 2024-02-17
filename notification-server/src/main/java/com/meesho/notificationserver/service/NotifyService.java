package com.meesho.notificationserver.service;

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
    @Autowired
    private NotifyRepo notifyRepo;
    @Autowired
    private ModelMapper modelMapper;
    private Logger logger = LoggerFactory.getLogger(NotifyService.class);
    public NotifyDto saveNotification (NotifyDto notifyDto){
        NotifyDto savednotifyDto = NotifyDto.builder().build();
        if( notifyDto.getPhoneNumber()==null|| notifyDto.getPhoneNumber().isEmpty() || notifyDto.getPhoneNumber().isBlank()){
            throw new InvalidRequestException("INVALID_REQUEST","Phone number is mandatory");
        }
        try {
            Notify notify = this.modelMapper.map(notifyDto, Notify.class);
            LocalDateTime currentDateTime = LocalDateTime.now();
            notify.setCreatedAt(currentDateTime);
            notify.setStatus("sent");
            Notify savedNotify = notifyRepo.save(notify);
            logger.info(String.format("notification saved in db for the phone number %s with the unique id %s", notify.getPhoneNumber(), notify.getId()));
            savednotifyDto = this.modelMapper.map(savedNotify, NotifyDto.class);
        }
        catch(Exception nex){
            logger.error("could not save the number due to some internal error , StackTrace {}", ExceptionUtils.getStackTrace(nex));
        }
        return savednotifyDto;

    }
    public void deleteNotification(UUID requestID){
        Notify notify = this.notifyRepo.findById(requestID).orElseThrow(()->new ResourceNotFoundException("NOT_FOUND","The requested Id to be deleted no found"));
        notifyRepo.delete(notify);
    }



}
