package com.meesho.notificationconsumer.entity.sql;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class Notify {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String phoneNumber;
    private String message;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
