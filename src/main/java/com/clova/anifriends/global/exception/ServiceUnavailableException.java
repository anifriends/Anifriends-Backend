package com.clova.anifriends.global.exception;

public abstract class ServiceUnavailableException extends AniFriendsException {

    protected ServiceUnavailableException(ErrorCode errorCode, String message) {
        super(errorCode, message);

    }

}
