package com.clova.anifriends.domain.recruitment.support.fixture;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentResponse;

public class RecruitmentDtoFixture {

    public static FindRecruitmentResponse findRecruitmentResponse(Recruitment recruitment) {
        return FindRecruitmentResponse.from(recruitment);
    }

}
