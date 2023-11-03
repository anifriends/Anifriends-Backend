package com.clova.anifriends.domain.recruitment.support.fixture;

import com.clova.anifriends.domain.common.dto.PageInfo;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentByShelterResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentDetailByVolunteerResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByShelterResponse;
import org.springframework.data.domain.Page;

public class RecruitmentDtoFixture {

    public static FindRecruitmentByShelterResponse findRecruitmentResponse(
        Recruitment recruitment) {
        return FindRecruitmentByShelterResponse.from(recruitment);
    }

    public static FindRecruitmentDetailByVolunteerResponse findRecruitmentByVolunteerResponse(
        Recruitment recruitment) {
        return FindRecruitmentDetailByVolunteerResponse.from(recruitment);
    }

    public static FindRecruitmentsByShelterResponse findRecruitmentsByShelterResponse(
        Page<Recruitment> pageResult) {
        return FindRecruitmentsByShelterResponse.of(pageResult.getContent(), PageInfo.from(pageResult));
    }
}
