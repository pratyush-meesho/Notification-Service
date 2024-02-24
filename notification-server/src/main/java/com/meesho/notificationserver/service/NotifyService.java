package com.meesho.notificationserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.meesho.notificationserver.exception.PhoneNumberInvalidException;
import com.meesho.notificationserver.payload.dto.NotifyDto;

import java.util.UUID;

public interface NotifyService {
    NotifyDto saveNotification(NotifyDto notifyDto) throws JsonProcessingException, PhoneNumberInvalidException;

    void deleteNotification(UUID requestID);
}
