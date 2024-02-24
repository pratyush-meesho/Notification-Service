package com.meesho.notificationserver.entity.sql;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
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
