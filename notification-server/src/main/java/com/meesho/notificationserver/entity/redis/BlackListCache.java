package com.meesho.notificationserver.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@RedisHash("BlackListCache")
public class BlackListCache implements Serializable {
    @Id
    private String phoneNumber;
}