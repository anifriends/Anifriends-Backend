package com.clova.anifriends.domain.applicant.exception;

import com.clova.anifriends.global.exception.ConflictException;
import com.clova.anifriends.global.exception.ErrorCode;

public class ApplicantCanNotApplyException extends ConflictException {

    public ApplicantCanNotApplyException(ErrorCode errorCode,
        String message) {
        super(errorCode, message);
    }
}
