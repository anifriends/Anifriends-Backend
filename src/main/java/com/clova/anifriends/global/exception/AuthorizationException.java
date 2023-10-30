package com.clova.anifriends.global.exception;

public abstract class AuthorizationException extends RuntimeException {

    protected AuthorizationException(String message) {
        super(message);
    }
}
