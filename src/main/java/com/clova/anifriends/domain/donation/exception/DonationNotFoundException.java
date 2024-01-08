package com.clova.anifriends.domain.donation.exception;

import static com.clova.anifriends.global.exception.ErrorCode.NOT_FOUND;

import com.clova.anifriends.global.exception.NotFoundException;

public class DonationNotFoundException extends NotFoundException {

    public DonationNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
