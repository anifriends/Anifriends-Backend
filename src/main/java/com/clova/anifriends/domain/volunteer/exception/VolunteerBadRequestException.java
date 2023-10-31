package com.clova.anifriends.domain.volunteer.exception;

import com.clova.anifriends.global.exception.BadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;

public class VolunteerBadRequestException extends BadRequestException {

    public VolunteerBadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
