package com.meesho.notificationserver.exception;

public class ElasticSearchException extends RuntimeException {
    public ElasticSearchException(String message) {
        super(message);
    }
}