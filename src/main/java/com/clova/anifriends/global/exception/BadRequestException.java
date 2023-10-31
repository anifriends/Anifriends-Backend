package com.clova.anifriends.global.exception;

public abstract class BadRequestException extends AniFriendsException {

    protected BadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
