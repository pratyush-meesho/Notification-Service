package com.meesho.notificationserver.repository;

import com.meesho.notificationserver.entity.Sms;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SmsRepo extends ElasticsearchRepository<Sms, UUID> {
    List<Sms> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<Sms>findByUpdatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<Sms>findByMessageContainingIgnoreCase(String msg);


}
