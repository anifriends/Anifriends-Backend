package com.clova.anifriends.domain.recruitment.exception;

import com.clova.anifriends.global.exception.NotFoundException;

public class RecruitmentNotFoundException extends NotFoundException {

    public RecruitmentNotFoundException(String message) {
        super(message);
    }
}
