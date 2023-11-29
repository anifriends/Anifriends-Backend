package com.clova.anifriends.domain.applicant.support;

import com.clova.anifriends.domain.applicant.repository.response.FindApplyingVolunteerResult;
import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.jetbrains.annotations.NotNull;

public class ApplicantDtoFixture {

    private static final Long SHELTER_ID = 1L;
    private static final Long RECRUITMENT_ID = 1L;
    private static final Long APPLICANT_ID = 1L;
    private static final String RECRUITMENT_TITLE = "모집글 제목";
    private static final String SHELTER_NAME = "보호소 제목";
    private static final ApplicantStatus APPLICANT_STATUS = ApplicantStatus.APPROVED;
    private static final boolean APPLICANT_IS_WRITED_REVIEW = true;
    private static final LocalDateTime RECRUITMENT_START_TIME = LocalDateTime.now();

    public static List<FindApplyingVolunteerResult> findApplyingVolunteerResults(int end) {
        return IntStream.range(0, end)
            .mapToObj(i -> getFindApplyingVolunteerResult())
            .toList();
    }

    @NotNull
    private static FindApplyingVolunteerResult getFindApplyingVolunteerResult() {
        return new FindApplyingVolunteerResult() {

            @Override
            public Long getShelterId() {
                return SHELTER_ID;
            }

            @Override
            public Long getRecruitmentId() {
                return RECRUITMENT_ID;
            }

            @Override
            public Long getApplicantId() {
                return APPLICANT_ID;
            }

            @Override
            public String getRecruitmentTitle() {
                return RECRUITMENT_TITLE;
            }

            @Override
            public String getShelterName() {
                return SHELTER_NAME;
            }

            @Override
            public ApplicantStatus getApplicantStatus() {
                return APPLICANT_STATUS;
            }

            @Override
            public boolean getApplicantIsWritedReview() {
                return APPLICANT_IS_WRITED_REVIEW;
            }

            @Override
            public LocalDateTime getRecruitmentStartTime() {
                return RECRUITMENT_START_TIME;
            }
        };
    }


}
