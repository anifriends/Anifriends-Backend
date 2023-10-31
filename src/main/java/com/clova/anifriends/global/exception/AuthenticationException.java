package com.clova.anifriends.global.exception;

public abstract class AuthenticationException extends AniFriendsException {

    protected AuthenticationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
