package com.clova.anifriends.domain.recruitment.support.fixture;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentDetailResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindShelterRecruitmentsResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByShelterResponse;
import org.springframework.data.domain.Page;

public class RecruitmentDtoFixture {

    public static FindRecruitmentsByShelterResponse findRecruitmentsByShelterResponse(
        Page<Recruitment> pageResult) {
        return FindRecruitmentsByShelterResponse.from(pageResult);
    }

    public static FindShelterRecruitmentsResponse FindShelterRecruitmentsResponse(
        Page<Recruitment> pageResult) {
        return FindShelterRecruitmentsResponse.from(pageResult);
    }

    public static FindRecruitmentDetailResponse findRecruitmentDetailResponse(
        Recruitment recruitment
    ) {
        return FindRecruitmentDetailResponse.from(recruitment);
    }
}
