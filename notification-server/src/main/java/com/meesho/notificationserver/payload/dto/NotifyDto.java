package com.meesho.notificationserver.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class NotifyDto {
    private UUID id;
    private String phoneNumber;
    private String message;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
