package com.clova.anifriends.global.exception;

public abstract class NotFoundException extends AniFriendsException {

    protected NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
