package com.clova.anifriends.domain.shelter.exception;

import com.clova.anifriends.global.exception.BadRequestException;

public class ShelterBadRequestException extends BadRequestException {

    public ShelterBadRequestException(String message) {
        super(message);
    }
}
