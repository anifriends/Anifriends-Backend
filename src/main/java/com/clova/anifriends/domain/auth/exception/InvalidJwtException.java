package com.clova.anifriends.domain.auth.exception;

import com.clova.anifriends.global.exception.AuthenticationException;
import com.clova.anifriends.global.exception.ErrorCode;

public class InvalidJwtException extends AuthenticationException {

    public InvalidJwtException(String message) {
        super(ErrorCode.UN_AUTHENTICATION, message);
    }

    public InvalidJwtException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
