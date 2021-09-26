package com.zuzex.vvolkov.exceptions;

public class UserNotExistsException extends RuntimeException {
    public UserNotExistsException(String username) {
        super(username);
    }
}