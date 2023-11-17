package com.clova.anifriends.domain.auth.exception;

import com.clova.anifriends.global.exception.AuthorizationException;
import com.clova.anifriends.global.exception.ErrorCode;

public class ExpiredRefreshTokenException extends AuthorizationException {

    public ExpiredRefreshTokenException(String message) {
        super(ErrorCode.REFRESH_TOKEN_EXPIRED, message);
    }
}
