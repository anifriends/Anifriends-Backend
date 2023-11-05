package com.clova.anifriends.domain.review.exception;

import com.clova.anifriends.global.exception.AuthorizationException;
import com.clova.anifriends.global.exception.ErrorCode;

public class ReviewAuthorizationException extends AuthorizationException {

    public ReviewAuthorizationException(String message) {
        super(ErrorCode.UN_AUTHORIZATION, message);
    }
}
