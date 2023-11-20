package com.clova.anifriends.global.aspect;

import com.clova.anifriends.global.exception.ConflictException;
import com.clova.anifriends.global.exception.ErrorCode;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataIntegrityHandler {

    String message() default "";

    ErrorCode errorCode() default ErrorCode.CONCURRENCY;

    Class<? extends Throwable> exceptionClass() default ConflictException.class;

}
