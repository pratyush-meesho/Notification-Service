package com.meesho.notificationserver.service;

import com.meesho.notificationserver.entity.Sms;
import com.meesho.notificationserver.repository.SmsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ElasticService {
    @Autowired
    private SmsRepo smsRepo;
    private static final Logger logger = LoggerFactory.getLogger(ElasticService.class);
    public List<Sms> findByStartDateAndEndDateForCreation(LocalDateTime st,LocalDateTime et){
        List<Sms> l = smsRepo.findByCreatedAtBetween(st,et);
        return l;
    }
    public List<Sms> findByStartDateAndEndDateForUpdate(LocalDateTime st,LocalDateTime et){
        List<Sms> l = smsRepo.findByUpdatedAtBetween(st,et);
        return l;
    }
    public List<Sms> findByStringContaining(String small_message){
        List<Sms>l = smsRepo.findByMessageContainingIgnoreCase(small_message);
        return l;
    }

}
