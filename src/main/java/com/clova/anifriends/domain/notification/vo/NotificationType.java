package com.clova.anifriends.domain.notification.vo;

import com.clova.anifriends.domain.common.EnumType;

public enum NotificationType implements EnumType {

    // 보호소
    NEW_APPLICANT("님께서 지원하셨습니다."),
    APPLICANT_FULL("마감되었습니다."),
    ENCOURAGE_CHECK_ATTENDANCE("봉사가 시작되었습니다. 출석 확인을 진행해주세요!"),
    NEW_SHELTER_REVIEW("보호소에 새로운 후기가 등록되었습니다."),

    // 봉사자
    VOLUNTEER_APPROVED("봉사 신청이 승인되었습니다."),
    VOLUNTEER_REFUSED("봉사 신청이 거절되었습니다."),
    A_DAY_BEFORE_VOLUNTEER("봉사 하루 전입니다."),
    THREE_DAY_BEFORE_VOLUNTEER("봉사 3일 전입니다."),
    INCREASE_VOLUNTEER_TEMPERATURE("'C의 봉사자 온도가 상승하였습니다."),
    ENCOURAGE_WRITE_REVIEW("봉사 후기를 작성해주세요.");

    private final String message;

    NotificationType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
