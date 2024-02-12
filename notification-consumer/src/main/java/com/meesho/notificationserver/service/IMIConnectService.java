package com.meesho.notificationserver.service;

import com.meesho.notificationserver.entity.Notify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class IMIConnectService {
    private final RestTemplate restTemplate;
    @Autowired
    public IMIConnectService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendNotification(String PhoneNumber,String text,UUID id) {
        String apiUrl = "https://api.imiconnect.in/resources/v1/messaging";
        // Create request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("deliverychannel", "sms");
        Map<String, Object> channels = new HashMap<>();
        Map<String, Object> smsChannel = new HashMap<>();
        smsChannel.put("text",text);
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
        // Add your custom header here
        headers.set("Your-Header-Name", "Your-Header-Value");

        // Create HTTP entity with headers and request body
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        restTemplate.postForEntity(apiUrl, requestEntity, Void.class);
    }




}
