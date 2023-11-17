package com.clova.anifriends.global.image;

import static com.clova.anifriends.global.exception.ErrorCode.BAD_REQUEST;

import com.clova.anifriends.global.exception.BadRequestException;

public class S3BadRequestException extends BadRequestException {

    public S3BadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
