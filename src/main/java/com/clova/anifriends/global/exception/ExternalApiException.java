package com.clova.anifriends.global.exception;

import static com.clova.anifriends.global.exception.ErrorCode.SERVICE_UNAVAILABLE;

public class ExternalApiException extends ServiceUnavailableException {

    public ExternalApiException(String message) {
        super(SERVICE_UNAVAILABLE, message);
    }
}
