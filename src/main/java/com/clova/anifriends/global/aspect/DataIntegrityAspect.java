package com.clova.anifriends.global.aspect;

import com.clova.anifriends.global.exception.ErrorCode;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataIntegrityAspect {

    @AfterThrowing(pointcut = "@annotation(dataIntegrityHandler)", throwing = "exception")
    public void handleDataIntegrityViolation(DataIntegrityHandler dataIntegrityHandler,
        DataIntegrityViolationException exception)
        throws Throwable {
        String message = dataIntegrityHandler.message();
        ErrorCode errorCode = dataIntegrityHandler.errorCode();
        Class<? extends Throwable> exceptionClass = dataIntegrityHandler.exceptionClass();

        throw exceptionClass.getDeclaredConstructor(ErrorCode.class, String.class)
            .newInstance(errorCode, message);
    }

}
