package com.meesho.notificationconsumer.service;

import java.util.UUID;

public interface IMIConnectService {
    void sendNotification(String PhoneNumber, String text, UUID id);
}
