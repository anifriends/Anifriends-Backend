package com.clova.anifriends.domain.recruitment.support.fixture;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentByShelterResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentDetailByVolunteerResponse;

public class RecruitmentDtoFixture {

    public static FindRecruitmentByShelterResponse findRecruitmentResponse(
        Recruitment recruitment) {
        return FindRecruitmentByShelterResponse.from(recruitment);
    }

    public static FindRecruitmentDetailByVolunteerResponse findRecruitmentByVolunteerResponse(
        Recruitment recruitment) {
        return FindRecruitmentDetailByVolunteerResponse.from(recruitment);
    }
}
