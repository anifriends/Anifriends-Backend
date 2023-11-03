package com.clova.anifriends.domain.review.exception;

import static com.clova.anifriends.global.exception.ErrorCode.BAD_REQUEST;

import com.clova.anifriends.global.exception.BadRequestException;

public class ReviewBadRequestException extends BadRequestException {

    public ReviewBadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
