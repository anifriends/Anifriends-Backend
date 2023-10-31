package com.clova.anifriends.global.exception;

public abstract class AniFriendsException extends RuntimeException {

    private final ErrorCode errorCode;

    protected AniFriendsException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode.getValue();
    }
}
