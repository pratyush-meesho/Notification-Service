package com.meesho.notificationserver.service.impl;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import com.meesho.notificationserver.entity.elasticsearch.Sms;
import com.meesho.notificationserver.exception.InvalidRequestException;
import com.meesho.notificationserver.repository.SmsRepo;
import com.meesho.notificationserver.service.ElasticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ElasticServiceImpl implements ElasticService {
    @Autowired
    private SmsRepo smsRepo;

    public List<Sms> findByStartDateAndEndDateForCreation(LocalDateTime st, LocalDateTime et) throws ElasticsearchException {
        if (st == null || et == null) {
            throw new InvalidRequestException("INVALID_REQUEST", "start date or end date is null");
        }
        if (st.isAfter(et)) {
            throw new IllegalArgumentException("Invalid start or end date-time");
        }
        return smsRepo.findByCreatedAtBetween(st, et);
    }

    public List<Sms> findByStartDateAndEndDateForUpdate(LocalDateTime st, LocalDateTime et) throws ElasticsearchException {
        if (st == null || et == null) {
            throw new InvalidRequestException("INVALID_REQUEST", "start date or end date is null");
        }
        if (st.isAfter(et)) {
            throw new IllegalArgumentException("Invalid start or end date-time");
        }
        return smsRepo.findByUpdatedAtBetween(st, et);
    }

    public List<Sms> findByStringContaining(String small_message) throws ElasticsearchException {
        if (small_message == null || small_message.isEmpty())
            throw new InvalidRequestException("INVALID_REQUEST", "start date or end date is null");
        return smsRepo.findByMessageContainingIgnoreCase(small_message);
    }

}
