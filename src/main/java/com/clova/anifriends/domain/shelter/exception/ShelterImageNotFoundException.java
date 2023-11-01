package com.clova.anifriends.domain.shelter.exception;

import com.clova.anifriends.global.exception.ErrorCode;
import com.clova.anifriends.global.exception.NotFoundException;

public class ShelterImageNotFoundException extends NotFoundException {

    public ShelterImageNotFoundException(ErrorCode errorCode,
        String message) {
        super(errorCode, message);
    }
}
