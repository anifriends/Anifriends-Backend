package com.clova.anifriends.global.exception;

public abstract class AuthorizationException extends AniFriendsException {

    protected AuthorizationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
