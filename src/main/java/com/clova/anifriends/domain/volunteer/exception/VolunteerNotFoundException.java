package com.clova.anifriends.domain.volunteer.exception;

import static com.clova.anifriends.global.exception.ErrorCode.NOT_FOUND;

import com.clova.anifriends.global.exception.NotFoundException;

public class VolunteerNotFoundException extends NotFoundException {

    public VolunteerNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
