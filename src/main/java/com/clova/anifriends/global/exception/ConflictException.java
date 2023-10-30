package com.clova.anifriends.global.exception;

public abstract class ConflictException extends RuntimeException {

    protected ConflictException(String message) {
        super(message);
    }
}
