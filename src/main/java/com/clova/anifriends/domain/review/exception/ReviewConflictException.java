package com.clova.anifriends.domain.review.exception;

import com.clova.anifriends.global.exception.ConflictException;
import com.clova.anifriends.global.exception.ErrorCode;

public class ReviewConflictException extends ConflictException {

    public ReviewConflictException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
