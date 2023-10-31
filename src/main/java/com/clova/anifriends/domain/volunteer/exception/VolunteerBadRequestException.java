package com.clova.anifriends.domain.volunteer.exception;

import com.clova.anifriends.global.exception.BadRequestException;

public class VolunteerBadRequestException extends BadRequestException {

    public VolunteerBadRequestException(String message) {
        super(message);
    }
}
