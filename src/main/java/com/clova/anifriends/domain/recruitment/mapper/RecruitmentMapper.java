package com.clova.anifriends.domain.recruitment.mapper;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.request.RegisterRecruitmentRequest;
import com.clova.anifriends.domain.shelter.Shelter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class RecruitmentMapper {

    public static Recruitment toRecruitment(Shelter shelter, RegisterRecruitmentRequest request) {
        return new Recruitment(
            shelter,
            request.title(),
            request.capacity(),
            request.content(),
            request.startTime(),
            request.endTime(),
            request.deadline(),
            request.imageUrls());
    }
}
