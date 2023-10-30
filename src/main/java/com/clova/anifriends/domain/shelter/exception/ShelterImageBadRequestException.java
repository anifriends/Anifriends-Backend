package com.clova.anifriends.domain.shelter.exception;

import com.clova.anifriends.global.exception.BadRequestException;

public class ShelterImageBadRequestException extends BadRequestException {

    public ShelterImageBadRequestException(String message) {
        super(message);
    }
}
