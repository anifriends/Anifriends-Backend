package com.clova.anifriends.domain.recruitment.dto.response;

import com.clova.anifriends.domain.common.PageInfo;
import com.clova.anifriends.domain.recruitment.Recruitment;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindCompletedRecruitmentsResponse(
    List<FindCompletedRecruitmentResponse> recruitments,
    PageInfo pageInfo) {

    public record FindCompletedRecruitmentResponse(
        Long recruitmentId,
        String recruitmentTitle,
        LocalDateTime recruitmentStartTime,
        String shelterName) {

        public static FindCompletedRecruitmentResponse from(Recruitment recruitment) {
            return new FindCompletedRecruitmentResponse(
                recruitment.getRecruitmentId(),
                recruitment.getTitle(),
                recruitment.getStartTime(),
                recruitment.getShelter().getName());
        }
    }

    public static FindCompletedRecruitmentsResponse from(Page<Recruitment> recruitmentPage) {
        PageInfo pageInfo = PageInfo.of(
            recruitmentPage.getTotalElements(),
            recruitmentPage.hasNext());
        List<FindCompletedRecruitmentResponse> recruitments = recruitmentPage
            .map(FindCompletedRecruitmentResponse::from)
            .stream().toList();
        return new FindCompletedRecruitmentsResponse(recruitments, pageInfo);
    }
}
