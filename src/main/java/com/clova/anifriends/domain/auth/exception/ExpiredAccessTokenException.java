package com.clova.anifriends.domain.auth.exception;

import com.clova.anifriends.global.exception.AuthenticationException;
import com.clova.anifriends.global.exception.ErrorCode;

public class ExpiredAccessTokenException extends AuthenticationException {

    public ExpiredAccessTokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
