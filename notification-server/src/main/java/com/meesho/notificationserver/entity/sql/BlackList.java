package com.meesho.notificationserver.entity.sql;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String phoneNumber;

}
