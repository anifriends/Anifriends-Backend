package com.clova.anifriends.domain.donation.exception;

import com.clova.anifriends.global.exception.BadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;

public class DonationDuplicateException extends BadRequestException {

    public DonationDuplicateException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }
}
