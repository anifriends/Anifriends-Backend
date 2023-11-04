package com.clova.anifriends.global.exception;

import com.clova.anifriends.domain.common.EnumType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode implements EnumType {

    // 400
    BAD_REQUEST("AF001"), // 잘못된 입력값
    INVALID_AUTH_INFO("AF002"), // 아이디 비밀번호 매치 안됨
    // 401
    TOKEN_EXPIRED("AF101"), // 토큰 만료
    UN_AUTHENTICATION("AF102"), // 인증 안됨
    // 403
    UN_AUTHORIZATION("AF301"), // 권한 없음
    // 404
    NOT_FOUND("AF401"), // 존재하지 않는 리소스
    // 409
    ALREADY_EXISTS("AF901"), // 이미 존재하는 리소스
    CONCURRENCY("AF902"), // 선착순 마감
    // 500
    INTERNAL_SERVER_ERROR("AF999"); // 서버 내부 에러

    private final String value;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }
}
