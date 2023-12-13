package com.clova.anifriends.domain.payment.exception;

import static com.clova.anifriends.global.exception.ErrorCode.SERVICE_UNAVAILABLE;

import com.clova.anifriends.global.exception.ServiceUnavailableException;

public class PaymentFailException extends ServiceUnavailableException {

    public PaymentFailException(String message) {
        super(SERVICE_UNAVAILABLE, message);
    }
}
