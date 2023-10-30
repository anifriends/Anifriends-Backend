package com.clova.anifriends.global.exception;

public abstract class AuthenticationException extends RuntimeException {

    protected AuthenticationException(String message) {
        super(message);
    }
}
