package com.clova.anifriends.domain.shelter.exception;

import com.clova.anifriends.global.exception.ConflictException;
import com.clova.anifriends.global.exception.ErrorCode;

public class ShelterConflictException extends ConflictException {

    public ShelterConflictException(String message) {
        super(ErrorCode.ALREADY_EXISTS, message);
    }
}
