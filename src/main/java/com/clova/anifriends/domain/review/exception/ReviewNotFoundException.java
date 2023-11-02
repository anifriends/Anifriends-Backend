package com.clova.anifriends.domain.review.exception;

import static com.clova.anifriends.global.exception.ErrorCode.NOT_FOUND;

import com.clova.anifriends.global.exception.NotFoundException;

public class ReviewNotFoundException extends NotFoundException {

    public ReviewNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
