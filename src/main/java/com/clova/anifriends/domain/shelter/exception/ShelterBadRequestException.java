package com.clova.anifriends.domain.shelter.exception;

import com.clova.anifriends.global.exception.BadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;

public class ShelterBadRequestException extends BadRequestException {

    public ShelterBadRequestException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }

    public ShelterBadRequestException(ErrorCode errorCode,
        String message) {
        super(errorCode, message);
    }
}
