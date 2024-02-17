package com.meesho.notificationserver.service;
import java.util.concurrent.*;

import com.meesho.notificationserver.entity.Sms;
import com.meesho.notificationserver.exception.CustomTimeoutException;
import com.meesho.notificationserver.exception.ElasticSearchException;
import com.meesho.notificationserver.repository.SmsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ElasticService {
    @Autowired
    private SmsRepo smsRepo;
    @Value("${api.timeout.time}")
    private Integer timeout;
    private static final Logger logger = LoggerFactory.getLogger(ElasticService.class);
    public List<Sms> findByStartDateAndEndDateForCreation(LocalDateTime st,LocalDateTime et){
        try{
            return executeWithTimeout(() -> smsRepo.findByCreatedAtBetween(st, et), timeout);
        }catch (CustomTimeoutException e) {
            //logger.error("Operation timed out while searching SMS by creation date", e);
            throw new CustomTimeoutException("Operation timed out while searching SMS by creation date");
        } catch (Exception ex) {
            //logger.error("Error occurred while searching SMS by creation date", e);
            throw new ElasticSearchException("Error occurred while searching SMS by creation date");
        }

    }
    public List<Sms> findByStartDateAndEndDateForUpdate(LocalDateTime st,LocalDateTime et){
        try{
            return executeWithTimeout(() -> smsRepo.findByUpdatedAtBetween(st, et), timeout);
        }catch (CustomTimeoutException e) {
            //logger.error("Operation timed out while searching SMS by creation date", e);
            throw new CustomTimeoutException("Operation timed out while searching SMS by creation date");
        } catch (Exception ex) {
            //logger.error("Error occurred while searching SMS by creation date", e);
            throw new ElasticSearchException("Error occurred while searching SMS by creation date");
        }
    }
    public List<Sms> findByStringContaining(String small_message){
        try{
            return executeWithTimeout(() -> smsRepo.findByMessageContainingIgnoreCase(small_message), timeout);
        }catch (CustomTimeoutException e) {
            //logger.error("Operation timed out while searching SMS by creation date", e);
            throw new CustomTimeoutException("Operation timed out while searching SMS by creation date");
        } catch (Exception ex) {
            //logger.error("Error occurred while searching SMS by creation date", e);
            throw new ElasticSearchException("Error occurred while searching SMS by creation date");
        }
    }
    private <T> T executeWithTimeout(Callable<T> task, long timeoutInMillis) {
        CompletableFuture<T> future = CompletableFuture.supplyAsync(() -> {
            try {
                return task.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        try {
            return future.get(timeoutInMillis, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new CustomTimeoutException("Operation timed out", e);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error executing task", e);
        }
    }

}
