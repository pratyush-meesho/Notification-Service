package com.meesho.notificationconsumer.service;

import com.meesho.notificationconsumer.payload.NotifyDto;

public interface SendAndCheckService {

    void updateAndValidate(NotifyDto receivednotifyDto);
}
