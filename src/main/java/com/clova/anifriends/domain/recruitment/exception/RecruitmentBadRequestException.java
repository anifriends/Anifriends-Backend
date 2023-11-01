package com.clova.anifriends.domain.recruitment.exception;

import com.clova.anifriends.global.exception.BadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;

public class RecruitmentBadRequestException extends BadRequestException {

    public RecruitmentBadRequestException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }
}
