package com.meesho.notificationserver.service;

import com.meesho.notificationserver.config.AppConstants;
import com.meesho.notificationserver.entity.BlackListCache;
import com.meesho.notificationserver.entity.Notify;
import com.meesho.notificationserver.entity.Sms;
import com.meesho.notificationserver.payload.NotifyDto;
import com.meesho.notificationserver.repository.BlackListCacheRepo;
import com.meesho.notificationserver.repository.BlackListRepo;
import com.meesho.notificationserver.repository.NotifyRepo;
import com.meesho.notificationserver.repository.SmsRepo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SendAndCheckService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private NotifyRepo notifyRepo;
    @Autowired
    private BlackListCacheRepo blackListCacheRepo;

    @Autowired
    private IMIConnectService iMIConnectService;

    @Autowired
    private SmsRepo smsRepo;
    private static final Logger logger = LoggerFactory.getLogger(SendAndCheckService.class);

    @KafkaListener(topics = AppConstants.TOPIC_NAME,
            groupId = AppConstants.GROUP_ID)
    public void consume(ConsumerRecord<String, NotifyDto> record) {
        logger.info(String.format("Message received -> %s", record.value().toString()));
        NotifyDto receivednotifyDto = record.value();
        Notify notify = notifyRepo.findById(receivednotifyDto.getId()).orElseThrow(() -> new RuntimeException("Not found"));
        LocalDateTime currentDateTime = LocalDateTime.now();
        notify.setUpdatedAt(currentDateTime);
        //saving it on elastic search
        Sms sms = new Sms();
        sms.setId(notify.getId());
        sms.setMessage(notify.getMessage());
        sms.setPhoneNumber(notify.getPhoneNumber());
        sms.setCreatedAt(notify.getCreatedAt());
        sms.setUpdatedAt(notify.getUpdatedAt());
        smsRepo.save(sms);
        notify.setStatus("received");
        notifyRepo.save(notify);//changing status of thr notify object there

        //Optional<BlackListCache>blackListCache  = blackListCacheRepo.findBlackListCacheByPhoneNumber( receivednotifyDto.getPhoneNumber());
        Optional<BlackListCache> blackListCache = Optional.ofNullable(blackListCacheRepo.findBlackListCacheByPhoneNumber(receivednotifyDto.getPhoneNumber()).orElse(null));
        if (blackListCache.isPresent()) {
            logger.info(String.format("the number %s is blacklisted", blackListCache.get().getPhoneNumber()));
            return;
        }
        iMIConnectService.sendNotification(notify.getPhoneNumber(),notify.getMessage(),notify.getId());
        logger.info("Message sent successfully ");
    }
}
