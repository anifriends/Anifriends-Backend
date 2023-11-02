package com.clova.anifriends.domain.applicant.exception;

import static com.clova.anifriends.global.exception.ErrorCode.BAD_REQUEST;

import com.clova.anifriends.global.exception.BadRequestException;

public class ApplicantBadRequestException extends BadRequestException {

    public ApplicantBadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
