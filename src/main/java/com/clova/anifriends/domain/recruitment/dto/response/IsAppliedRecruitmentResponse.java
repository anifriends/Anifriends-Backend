package com.clova.anifriends.domain.recruitment.dto.response;

public record IsAppliedRecruitmentResponse(boolean isAppliedRecruitment) {

    public static IsAppliedRecruitmentResponse from(boolean isAppliedRecruitment) {
        return new IsAppliedRecruitmentResponse(isAppliedRecruitment);
    }
}
