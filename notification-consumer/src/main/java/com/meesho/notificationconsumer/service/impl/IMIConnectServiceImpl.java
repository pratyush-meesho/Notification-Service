package com.meesho.notificationconsumer.service.impl;

import com.meesho.notificationconsumer.service.IMIConnectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class IMIConnectServiceImpl implements IMIConnectService {
    private final RestTemplate restTemplate;

    @Value("${imiconnectservice.header.value}")
    private String header_value;

    public IMIConnectServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendNotification(String PhoneNumber, String text, UUID id) {
        String apiUrl = "https://api.imiconnect.in/resources/v1/messaging";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("deliverychannel", "sms");
        Map<String, Object> channels = new HashMap<>();
        Map<String, Object> smsChannel = new HashMap<>();
        smsChannel.put("text", text);
        channels.put("sms", smsChannel);
        requestBody.put("channels", channels);
        List<Map<String, Object>> destinations = new ArrayList<>();
        Map<String, Object> destination = new HashMap<>();
        destination.put("msisdn", Collections.singletonList(PhoneNumber));
        destination.put("correlationId", id.toString());
        destinations.add(destination);
        requestBody.put("destination", destinations);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Key", header_value);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<?> x = restTemplate.postForEntity(apiUrl, requestEntity, Void.class);
        log.info("The response body is this {}", x);

    }


}
