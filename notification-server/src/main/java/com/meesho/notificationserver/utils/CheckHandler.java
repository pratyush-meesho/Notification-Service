package com.meesho.notificationserver.utils;

import com.meesho.notificationserver.exception.PhoneNumberInvalidException;

import java.util.regex.Pattern;

public class CheckHandler {
    public static void phoneNumCheckValid(String phoneNumber) throws PhoneNumberInvalidException {
        String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$|^\\d{10}$";
        if (!phoneNumber.matches(regex)) {
            throw new PhoneNumberInvalidException("Invalid phone number: " + phoneNumber);
        }

    }
}
