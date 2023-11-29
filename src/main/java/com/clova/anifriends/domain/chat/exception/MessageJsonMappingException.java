package com.clova.anifriends.domain.chat.exception;

import com.clova.anifriends.global.exception.ErrorCode;
import com.clova.anifriends.global.exception.JsonMappingException;

public class MessageJsonMappingException extends JsonMappingException {

    public MessageJsonMappingException(String message) {
        super(ErrorCode.INTERNAL_SERVER_ERROR, message);
    }
}
