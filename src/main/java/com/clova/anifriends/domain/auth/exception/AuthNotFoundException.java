package com.clova.anifriends.domain.auth.exception;

import com.clova.anifriends.global.exception.ErrorCode;
import com.clova.anifriends.global.exception.NotFoundException;

public class AuthNotFoundException extends NotFoundException {

    public AuthNotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }
}
