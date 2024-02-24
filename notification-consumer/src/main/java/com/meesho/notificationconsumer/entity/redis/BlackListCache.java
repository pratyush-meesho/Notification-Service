package com.meesho.notificationconsumer.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@RedisHash("BlackListCache")
public class BlackListCache implements Serializable {
    @Id
    private String phoneNumber;
}