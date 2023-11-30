package com.clova.anifriends.domain.recruitment.service;

public record IsAppliedRecruitmentResponse(boolean isAppliedRecruitment) {

    public static IsAppliedRecruitmentResponse from(boolean isAppliedRecruitment) {
        return new IsAppliedRecruitmentResponse(isAppliedRecruitment);
    }
}
