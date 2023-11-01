package com.clova.anifriends.domain.auth.exception;

import com.clova.anifriends.global.exception.AuthorizationException;
import com.clova.anifriends.global.exception.ErrorCode;

public class ExpiredRefreshTokenException extends AuthorizationException {

    public ExpiredRefreshTokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
