package com.clova.anifriends.domain.animal.exception;

import static com.clova.anifriends.global.exception.ErrorCode.NOT_FOUND;

import com.clova.anifriends.global.exception.NotFoundException;

public class NotFoundAnimalException extends NotFoundException {

    public NotFoundAnimalException(String message) {
        super(NOT_FOUND, message);
    }
}
