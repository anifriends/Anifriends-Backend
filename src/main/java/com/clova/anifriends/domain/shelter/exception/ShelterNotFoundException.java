package com.clova.anifriends.domain.shelter.exception;

import com.clova.anifriends.global.exception.ErrorCode;
import com.clova.anifriends.global.exception.NotFoundException;

public class ShelterNotFoundException extends NotFoundException {

    public ShelterNotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }
}
