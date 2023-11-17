package com.clova.anifriends.domain.volunteer.exception;

import com.clova.anifriends.global.exception.ErrorCode;
import com.clova.anifriends.global.exception.NotFoundException;

public class NotFoundVolunteerGenderException extends NotFoundException {

    public NotFoundVolunteerGenderException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
