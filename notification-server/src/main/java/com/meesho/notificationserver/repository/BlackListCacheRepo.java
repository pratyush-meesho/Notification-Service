package com.meesho.notificationserver.repository;

import com.meesho.notificationserver.entity.redis.BlackListCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlackListCacheRepo extends CrudRepository<BlackListCache, UUID> {
    Optional<BlackListCache> findBlackListCacheByPhoneNumber(String phoneNumber);


}
