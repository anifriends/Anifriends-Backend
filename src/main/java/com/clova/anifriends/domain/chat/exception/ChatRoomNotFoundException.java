package com.clova.anifriends.domain.chat.exception;

import static com.clova.anifriends.global.exception.ErrorCode.NOT_FOUND;

import com.clova.anifriends.global.exception.NotFoundException;

public class ChatRoomNotFoundException extends NotFoundException {

    public ChatRoomNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
