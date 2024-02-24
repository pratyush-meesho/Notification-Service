package com.meesho.notificationserver.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberListDto {
    private List<String> phoneNumbers;
}
