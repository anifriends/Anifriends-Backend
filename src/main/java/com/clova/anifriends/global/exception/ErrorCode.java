package com.clova.anifriends.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    // 400
    AF001, // 잘못된 입력값
    AF002, // 아이디 비밀번호 매치 안됨
    // 401
    AF101, // 토큰 만료
    AF102, // 인증 안됨
    // 403
    AF301, // 권한 없음
    // 404
    AF401, // 존재하지 않는 리소스
    // 409
    AF901, // 이미 존재하는 리소스
    AF902, // 선착순 마감
    // 500
    AF999; // 서버 내부 에러

}
