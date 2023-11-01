package com.clova.anifriends.domain.applicant.exception;

import com.clova.anifriends.global.exception.ConflictException;
import com.clova.anifriends.global.exception.ErrorCode;

public class ApplicantConflictException extends ConflictException {

    public ApplicantConflictException(ErrorCode errorCode,
        String message) {
        super(errorCode, message);
    }
}
