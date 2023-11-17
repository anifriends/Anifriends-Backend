package com.clova.anifriends.global.exception;

public abstract class ConflictException extends AniFriendsException {

    protected ConflictException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
