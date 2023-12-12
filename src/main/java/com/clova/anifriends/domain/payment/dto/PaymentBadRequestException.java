package com.clova.anifriends.domain.payment.dto;

import static com.clova.anifriends.global.exception.ErrorCode.BAD_REQUEST;

import com.clova.anifriends.global.exception.BadRequestException;

public class PaymentBadRequestException extends BadRequestException {

    public PaymentBadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
