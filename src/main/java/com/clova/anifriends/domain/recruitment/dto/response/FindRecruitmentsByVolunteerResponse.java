package com.clova.anifriends.domain.recruitment.dto.response;

import com.clova.anifriends.domain.common.PageInfo;
import com.clova.anifriends.domain.recruitment.Recruitment;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindRecruitmentsByVolunteerResponse(
    List<FindRecruitmentByVolunteerResponse> recruitments,
    PageInfo pageInfo) {

    public record FindRecruitmentByVolunteerResponse(
        Long recruitmentId,
        String recruitmentTitle,
        LocalDateTime recruitmentStartTime,
        LocalDateTime recruitmentEndTime,
        boolean recruitmentIsClosed,
        int recruitmentApplicantCount,
        int recruitmentCapacity,
        String shelterName,
        String shelterImageUrl) {

        public static FindRecruitmentByVolunteerResponse from(Recruitment recruitment) {
            return new FindRecruitmentByVolunteerResponse(
                recruitment.getRecruitmentId(),
                recruitment.getTitle(),
                recruitment.getStartTime(),
                recruitment.getEndTime(),
                recruitment.isClosed(),
                recruitment.getApplicantCount(),
                recruitment.getCapacity(),
                recruitment.getShelter().getName(),
                recruitment.getShelter().getShelterImageUrl()
            );
        }
    }

    public static FindRecruitmentsByVolunteerResponse from(Page<Recruitment> recruitments) {
        PageInfo pageInfo = PageInfo.of(recruitments.getTotalElements(), recruitments.hasNext());
        List<FindRecruitmentByVolunteerResponse> content = recruitments.getContent()
            .stream()
            .map(FindRecruitmentByVolunteerResponse::from)
            .toList();
        return new FindRecruitmentsByVolunteerResponse(content, pageInfo);
    }
}
