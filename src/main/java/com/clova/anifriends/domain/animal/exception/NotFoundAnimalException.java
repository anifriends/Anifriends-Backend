package com.clova.anifriends.domain.animal.exception;

import com.clova.anifriends.global.exception.ErrorCode;
import com.clova.anifriends.global.exception.NotFoundException;

public class NotFoundAnimalException extends NotFoundException {

    public NotFoundAnimalException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
