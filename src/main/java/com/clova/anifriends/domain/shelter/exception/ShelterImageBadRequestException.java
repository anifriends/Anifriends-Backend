package com.clova.anifriends.domain.shelter.exception;

import com.clova.anifriends.global.exception.BadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;

public class ShelterImageBadRequestException extends BadRequestException {
    
    public ShelterImageBadRequestException(ErrorCode errorCode,
        String message) {
        super(errorCode, message);
    }
}
