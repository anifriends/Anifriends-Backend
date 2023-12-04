package com.clova.anifriends.global.exception;

public class JsonMappingException extends AniFriendsException {

    protected JsonMappingException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
