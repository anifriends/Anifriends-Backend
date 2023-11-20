package com.clova.anifriends.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeEx(RuntimeException e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR.getValue(),
                "예측하지 못한 예외가 발생하였습니다." + e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestEx(BadRequestException e) {
        return ResponseEntity.status(BAD_REQUEST)
            .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationEx(AuthenticationException e) {
        return ResponseEntity.status(UNAUTHORIZED)
            .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> authorizationEx(AuthorizationException e) {
        return ResponseEntity.status(FORBIDDEN)
            .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundEx(NotFoundException e) {
        return ResponseEntity.status(NOT_FOUND)
            .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> conflictEx(ConflictException e) {
        return ResponseEntity.status(CONFLICT)
            .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        FieldError fieldError = Objects.requireNonNull(e.getFieldError());
        return ResponseEntity.status(BAD_REQUEST)
            .body(new ErrorResponse(ErrorCode.BAD_REQUEST.getValue(),
                String.format("%s. (%s)", fieldError.getDefaultMessage(), fieldError.getField())));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(
        MissingRequestHeaderException e) {
        return ResponseEntity.status(BAD_REQUEST)
            .body(new ErrorResponse(ErrorCode.BAD_REQUEST.getValue(),
                String.format("%s. (%s)", e.getMessage(), e.getHeaderName())));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestParameterException(
        MissingServletRequestParameterException e) {
        return ResponseEntity.status(BAD_REQUEST)
            .body(new ErrorResponse(ErrorCode.BAD_REQUEST.getValue(),
                String.format("%s. (%s)", e.getMessage(), e.getParameterName())));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedEx() {
        ErrorResponse errorResponse
            = new ErrorResponse(ErrorCode.UN_AUTHORIZATION.getValue(), "권한이 없습니다.");
        return ResponseEntity.status(FORBIDDEN)
            .body(errorResponse);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ErrorResponse> authenticationCredentialsNotFoundEx() {
        ErrorResponse errorResponse
            = new ErrorResponse(ErrorCode.UN_AUTHENTICATION.getValue(), "인증되지 않은 사용자입니다.");
        return ResponseEntity.status(UNAUTHORIZED)
            .body(errorResponse);
    }
}
