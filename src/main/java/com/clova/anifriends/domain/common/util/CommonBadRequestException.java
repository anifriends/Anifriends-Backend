package com.clova.anifriends.domain.common.util;

import com.clova.anifriends.global.exception.BadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;

public class CommonBadRequestException extends BadRequestException {

    protected CommonBadRequestException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }
}
