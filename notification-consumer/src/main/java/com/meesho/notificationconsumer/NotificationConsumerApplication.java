package com.meesho.notificationconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
public class NotificationConsumerApplication {
	public static void main(String[] args) {
		SpringApplication.run(NotificationConsumerApplication.class, args);
	}


}
