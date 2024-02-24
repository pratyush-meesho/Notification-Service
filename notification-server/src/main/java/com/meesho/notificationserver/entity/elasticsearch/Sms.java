package com.meesho.notificationserver.entity.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(indexName = "sms")
public class Sms {
    @Id
    private UUID id;
    private String phoneNumber;
    private String message;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime createdAt;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime updatedAt;
}
