package com.clova.anifriends.domain.animal.exception;

import com.clova.anifriends.global.exception.BadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;

public class AnimalBadRequestException extends BadRequestException {

    public AnimalBadRequestException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }
}
