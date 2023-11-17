package com.clova.anifriends.domain.auth.exception;

import com.clova.anifriends.global.exception.AuthenticationException;
import com.clova.anifriends.global.exception.ErrorCode;

public class AuthAuthenticationException extends AuthenticationException {

    public AuthAuthenticationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
