package com.clova.anifriends.domain.applicant.support;

import com.clova.anifriends.domain.applicant.repository.response.FindApplicantResult;
import com.clova.anifriends.domain.applicant.repository.response.FindApplyingVolunteerResult;
import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.jetbrains.annotations.NotNull;

public class ApplicantDtoFixture {

    private static final Long VOLUNTEER_ID = 1L;
    private static final Long SHELTER_ID = 1L;
    private static final Long RECRUITMENT_ID = 1L;
    private static final Long APPLICANT_ID = 1L;
    private static final String RECRUITMENT_TITLE = "모집글 제목";
    private static final String SHELTER_NAME = "보호소 제목";
    private static final ApplicantStatus APPLICANT_STATUS = ApplicantStatus.APPROVED;
    private static final boolean APPLICANT_IS_WRITED_REVIEW = true;
    private static final LocalDateTime RECRUITMENT_START_TIME = LocalDateTime.now();
    private static final LocalDate VOLUNTEER_BIRTH_DATE = LocalDate.now();
    private static final VolunteerGender VOLUNTEER_GENDER = VolunteerGender.MALE;
    private static final int COMPLETED_VOLUNTEER_COUNT = 5;
    private static final int VOLUNTEER_TEMPERATURE = 36;
    private static final String VOLUNTEER_NAME = "봉사자 이름";

    public static List<FindApplyingVolunteerResult> findApplyingVolunteerResults(int end) {
        return IntStream.range(0, end)
            .mapToObj(i -> findApplyingVolunteerResult())
            .toList();
    }

    public static List<FindApplicantResult> findApplicantResults(int end) {
        return IntStream.range(0, end)
            .mapToObj(i -> findApplicantResult())
            .toList();
    }

    @NotNull
    private static FindApplyingVolunteerResult findApplyingVolunteerResult() {
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

    private static FindApplicantResult findApplicantResult() {
        return new FindApplicantResult() {
            @Override
            public Long getVolunteerId() {
                return VOLUNTEER_ID;
            }

            @Override
            public Long getApplicantId() {
                return APPLICANT_ID;
            }

            @Override
            public String getVolunteerName() {
                return VOLUNTEER_NAME;
            }

            @Override
            public LocalDate getVolunteerBirthDate() {
                return VOLUNTEER_BIRTH_DATE;
            }

            @Override
            public VolunteerGender getVolunteerGender() {
                return VOLUNTEER_GENDER;
            }

            @Override
            public int getCompletedVolunteerCount() {
                return COMPLETED_VOLUNTEER_COUNT;
            }

            @Override
            public int getVolunteerTemperature() {
                return VOLUNTEER_TEMPERATURE;
            }

            @Override
            public ApplicantStatus getApplicantStatus() {
                return APPLICANT_STATUS;
            }
        };
    }
}
