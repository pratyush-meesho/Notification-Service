package com.meesho.notificationserver.service;

import com.meesho.notificationserver.entity.elasticsearch.Sms;

import java.time.LocalDateTime;
import java.util.List;

public interface ElasticService {
    List<Sms> findByStartDateAndEndDateForCreation(LocalDateTime st, LocalDateTime et);

    List<Sms> findByStartDateAndEndDateForUpdate(LocalDateTime st, LocalDateTime et);

    List<Sms> findByStringContaining(String small_message);
}
