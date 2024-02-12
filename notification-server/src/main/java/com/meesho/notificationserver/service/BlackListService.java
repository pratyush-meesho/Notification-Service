package com.meesho.notificationserver.service;

import com.meesho.notificationserver.entity.BlackList;
import com.meesho.notificationserver.entity.BlackListCache;
import com.meesho.notificationserver.exception.ResourceNotFoundException;
import com.meesho.notificationserver.repository.BlackListCacheRepo;
import com.meesho.notificationserver.repository.BlackListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlackListService {
    @Autowired
    BlackListRepo blackListRepo;
    @Autowired
    BlackListCacheRepo blackListCacheRepo;

    @Autowired
    RedisTemplate redisTemplate;
    public String AddPhoneNumber(String phoneNum){
        BlackList blackList = new BlackList();
        blackList.setPhoneNumber(phoneNum);
        BlackListCache blackListCache = new BlackListCache();
        blackListCache.setPhoneNumber(phoneNum);
        blackListCache.setId(blackList.getId());
        blackListCacheRepo.save(blackListCache);
        blackListRepo.save(blackList);
        return "SUCCESS";
    }
    public void DelPhoneNumber(String phoneNum){

        Optional<BlackListCache>blackListCache = Optional.ofNullable(blackListCacheRepo.findBlackListCacheByPhoneNumber(phoneNum).orElse(null));
        if(blackListCache.isPresent()){
            blackListCacheRepo.delete(blackListCache.get());
        }
        Optional<BlackList> blackList = Optional.ofNullable(blackListRepo.findBlackListsByPhoneNumber(phoneNum).orElse(null));
        if(blackList.isPresent()){
            blackListRepo.delete(blackList.get());
        }
        else{
            throw  new ResourceNotFoundException("NOT_FOUND","phone number not found to delete");
        }

    }
    public void AddPhoneNumberList(List<String> phoneNumbers){
        for(String phoneNumber: phoneNumbers){
            this.AddPhoneNumber(phoneNumber);
        }
    }
    public void DeletePhoneNumberList(List<String> phoneNumbers){
        for(String phoneNumber : phoneNumbers) {
            this.DelPhoneNumber(phoneNumber);
        }
    }





}
