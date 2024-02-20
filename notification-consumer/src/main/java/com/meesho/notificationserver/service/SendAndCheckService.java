package com.meesho.notificationserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.notificationserver.config.AppConstants;
import com.meesho.notificationserver.entity.BlackListCache;
import com.meesho.notificationserver.entity.Notify;
import com.meesho.notificationserver.entity.Sms;
import com.meesho.notificationserver.exception.ResourceNotFoundException;
import com.meesho.notificationserver.payload.NotifyDto;
import com.meesho.notificationserver.repository.BlackListCacheRepo;
import com.meesho.notificationserver.repository.BlackListRepo;
import com.meesho.notificationserver.repository.NotifyRepo;
import com.meesho.notificationserver.repository.SmsRepo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SendAndCheckService {
    private final ObjectMapper objectMapper;
    private ModelMapper modelMapper;
    private NotifyRepo notifyRepo;
    private BlackListCacheRepo blackListCacheRepo;

    private IMIConnectService iMIConnectService;

    private SmsRepo smsRepo;

    @Autowired
    public SendAndCheckService(ObjectMapper objectMapper, ModelMapper modelMapper, NotifyRepo notifyRepo, BlackListCacheRepo blackListCacheRepo, IMIConnectService iMIConnectService, SmsRepo smsRepo) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.notifyRepo = notifyRepo;
        this.blackListCacheRepo = blackListCacheRepo;
        this.iMIConnectService = iMIConnectService;
        this.smsRepo = smsRepo;
    }

    private static final Logger logger = LoggerFactory.getLogger(SendAndCheckService.class);
    public SendAndCheckService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }
//        private static final TypeReference<NotifyDto> KAFKA_ORDER_PAYLOAD_TYPE_REFERENCE = new TypeReference<NotifyDto>() {
//        };

    @KafkaListener(topics = AppConstants.TOPIC_NAME,
            groupId = AppConstants.GROUP_ID)
    public void consume(ConsumerRecord<String, Object> record) throws JsonProcessingException {
        try {
            NotifyDto receivednotifyDto = objectMapper.readValue(record.value().toString(), NotifyDto.class);
            //acknowledgment.acknowledge();
            logger.info("Message received {}",receivednotifyDto.toString());
            Notify notify = notifyRepo.findById(receivednotifyDto.getId()).orElseThrow(() -> new ResourceNotFoundException("NOT_FOUND", "Resource not found "));
            LocalDateTime currentDateTime = LocalDateTime.now();
            notify.setUpdatedAt(currentDateTime);
            Sms sms = Sms.builder().id(notify.getId()).message(notify.getMessage()).phoneNumber(notify.getPhoneNumber()).updatedAt(notify.getUpdatedAt()).createdAt(notify.getCreatedAt()).build();
            smsRepo.save(sms);
            notify.setStatus("received");
            notifyRepo.save(notify);//changing status of thr notify object there
            //Optional<BlackListCache>blackListCache  = blackListCacheRepo.findBlackListCacheByPhoneNumber( receivednotifyDto.getPhoneNumber());
            Optional<BlackListCache> blackListCache = Optional.ofNullable(blackListCacheRepo.findBlackListCacheByPhoneNumber(receivednotifyDto.getPhoneNumber()).orElse(null));
            if (blackListCache.isPresent()) {
                logger.info(String.format("the number %s is blacklisted", blackListCache.get().getPhoneNumber()));
                return;
            }
            iMIConnectService.sendNotification(notify.getPhoneNumber(), notify.getMessage(), notify.getId());
            logger.info("Message sent successfully ");


        }
        catch (Exception e){
            logger.error("Internal Server Error Occurred while consuming the message, StackTrace {}", ExceptionUtils.getStackTrace(e));
        }

    }
}
