package com.clova.anifriends.domain.recruitment.exception;

import com.clova.anifriends.global.exception.BadRequestException;

public class RecruitmentBadRequestException extends BadRequestException {

    public RecruitmentBadRequestException(String message) {
        super(message);
    }
}
