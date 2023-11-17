package com.clova.anifriends.domain.notification.exception;

import com.clova.anifriends.global.exception.BadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;

public class NotificationBadRequestException extends BadRequestException {

    public NotificationBadRequestException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }
}
