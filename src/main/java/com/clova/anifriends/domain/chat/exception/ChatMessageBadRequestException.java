package com.clova.anifriends.domain.chat.exception;

import static com.clova.anifriends.global.exception.ErrorCode.BAD_REQUEST;

import com.clova.anifriends.global.exception.BadRequestException;

public class ChatMessageBadRequestException extends BadRequestException {

    public ChatMessageBadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
