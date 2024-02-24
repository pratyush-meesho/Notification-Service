package com.meesho.notificationserver.service.impl;

import com.meesho.notificationserver.entity.sql.BlackList;
import com.meesho.notificationserver.entity.redis.BlackListCache;
import com.meesho.notificationserver.exception.InvalidRequestException;
import com.meesho.notificationserver.exception.PhoneNumberInvalidException;
import com.meesho.notificationserver.exception.ResourceNotFoundException;
import com.meesho.notificationserver.repository.BlackListCacheRepo;
import com.meesho.notificationserver.repository.BlackListRepo;
import com.meesho.notificationserver.service.BlackListService;
import com.meesho.notificationserver.utils.CheckHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlackListServiceImpl implements BlackListService {
    @Autowired
    BlackListRepo blackListRepo;
    @Autowired
    BlackListCacheRepo blackListCacheRepo;

    @Autowired
    RedisTemplate redisTemplate;

    public void addPhoneNumber(String phoneNum) throws PhoneNumberInvalidException {
        if (phoneNum.isEmpty())
            throw new InvalidRequestException("INVALID_REQUEST", "phone number is empty");
        CheckHandler.phoneNumCheckValid(phoneNum);
        BlackList blackList = new BlackList();
        blackList.setPhoneNumber(phoneNum);
        BlackListCache blackListCache = new BlackListCache();
        blackListCacheRepo.save(blackListCache);
        blackListRepo.save(blackList);

    }

    public void delPhoneNumber(String phoneNum) throws PhoneNumberInvalidException {
        if (phoneNum.isEmpty())
            throw new InvalidRequestException("INVALID_REQUEST", "phone number is empty");
        CheckHandler.phoneNumCheckValid(phoneNum);
        Optional<BlackListCache> blackListCache = blackListCacheRepo.findBlackListCacheByPhoneNumber(phoneNum);
        blackListCache.ifPresent(listCache -> blackListCacheRepo.delete(listCache));
        Optional<BlackList> blackList = Optional.ofNullable(blackListRepo.findBlackListsByPhoneNumber(phoneNum)).orElse(null);
        if (blackList.isEmpty()) {
            throw new ResourceNotFoundException("NOT_FOUND", "phone number not found to delete");
        } else {
            blackListRepo.delete(blackList.get());
        }
    }

    public void addPhoneNumberList(List<String> phoneNumbers) throws PhoneNumberInvalidException {
        for (String phoneNumber : phoneNumbers) {
            this.addPhoneNumber(phoneNumber);
        }
    }

    public void deletePhoneNumberList(List<String> phoneNumbers) throws PhoneNumberInvalidException {
        for (String phoneNumber : phoneNumbers) {
            this.delPhoneNumber(phoneNumber);
        }
    }


}
