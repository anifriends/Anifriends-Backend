package com.clova.anifriends.domain.animal.exception;

import static com.clova.anifriends.global.exception.ErrorCode.NOT_FOUND;

import com.clova.anifriends.global.exception.NotFoundException;

public class AnimalNotFoundException extends NotFoundException {

    public AnimalNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
