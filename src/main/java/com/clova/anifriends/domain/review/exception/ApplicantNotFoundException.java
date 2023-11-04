package com.clova.anifriends.domain.review.exception;

import static com.clova.anifriends.global.exception.ErrorCode.NOT_FOUND;

import com.clova.anifriends.global.exception.NotFoundException;

public class ApplicantNotFoundException extends NotFoundException {

    public ApplicantNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}

