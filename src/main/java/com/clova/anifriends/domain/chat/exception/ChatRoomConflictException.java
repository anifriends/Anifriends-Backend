package com.clova.anifriends.domain.chat.exception;

import com.clova.anifriends.global.exception.ConflictException;
import com.clova.anifriends.global.exception.ErrorCode;

public class ChatRoomConflictException extends ConflictException {

    public ChatRoomConflictException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
