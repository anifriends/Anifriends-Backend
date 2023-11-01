package com.clova.anifriends.domain.recruitment.exception;

import static com.clova.anifriends.global.exception.ErrorCode.NOT_FOUND;

import com.clova.anifriends.global.exception.NotFoundException;

public class RecruitmentNotFoundException extends NotFoundException {

    public RecruitmentNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}