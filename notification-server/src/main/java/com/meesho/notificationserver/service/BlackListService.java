package com.meesho.notificationserver.service;

import com.meesho.notificationserver.exception.PhoneNumberInvalidException;

public interface BlackListService {
    void addPhoneNumber(String phoneNum) throws PhoneNumberInvalidException;

    void delPhoneNumber(String phoneNum) throws PhoneNumberInvalidException;

}
