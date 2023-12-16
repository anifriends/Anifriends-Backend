package com.clova.anifriends.domain.donation.exception;

import static com.clova.anifriends.global.exception.ErrorCode.BAD_REQUEST;

import com.clova.anifriends.global.exception.BadRequestException;

public class DonationBadRequestException extends BadRequestException {

    public DonationBadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
