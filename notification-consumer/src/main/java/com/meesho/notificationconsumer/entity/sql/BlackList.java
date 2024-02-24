package com.meesho.notificationconsumer.entity.sql;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    String phoneNumber;
}
