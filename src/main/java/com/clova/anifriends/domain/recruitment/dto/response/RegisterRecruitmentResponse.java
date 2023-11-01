package com.clova.anifriends.domain.recruitment.dto.response;

import com.clova.anifriends.domain.recruitment.Recruitment;

public record RegisterRecruitmentResponse(Long recruitmentId) {

    public static RegisterRecruitmentResponse from(Recruitment recruitment) {
        return new RegisterRecruitmentResponse(recruitment.getRecruitmentId());
    }
}
