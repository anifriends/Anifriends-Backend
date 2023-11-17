package com.clova.anifriends.domain.chat.exception;

import com.clova.anifriends.global.exception.ErrorCode;
import com.clova.anifriends.global.exception.NotFoundException;

public class ChatNotFoundException extends NotFoundException {

    public ChatNotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }
}
