package com.clova.anifriends.domain.applicant.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.domain.applicant.dto.FindApplicantsResponse;
import com.clova.anifriends.domain.applicant.dto.FindApplicantsResponse.FindApplicantResponse;
import com.clova.anifriends.domain.applicant.dto.response.FindApplyingVolunteersResponse;
import com.clova.anifriends.domain.applicant.dto.response.FindApplyingVolunteersResponse.FindApplyingVolunteerResponse;
import com.clova.anifriends.domain.applicant.repository.response.FindApplicantResult;
import com.clova.anifriends.domain.applicant.repository.response.FindApplyingVolunteerResult;
import com.clova.anifriends.domain.applicant.support.ApplicantDtoFixture;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ApplicantMapperTest {

    @Nested
    @DisplayName("FindApplyingVolunteersResponse resultToResponse 메서드 실행 시")
    class FindApplyingVolunteersResponseTest {

        @Test
        @DisplayName("성공")
        void resultToResponse() {
            //given
            List<FindApplyingVolunteerResult> applyingVolunteerResults
                = ApplicantDtoFixture.findApplyingVolunteerResults(1);

            //when
            FindApplyingVolunteersResponse findApplyingVolunteersResponse
                = ApplicantMapper.resultToResponse(applyingVolunteerResults);

            //then
            FindApplyingVolunteerResult result = applyingVolunteerResults.get(0);
            FindApplyingVolunteerResponse response
                = findApplyingVolunteersResponse.applicants().get(0);
            assertThat(response.shelterId()).isEqualTo(result.getShelterId());
            assertThat(response.recruitmentId()).isEqualTo(result.getRecruitmentId());
            assertThat(response.applicantId()).isEqualTo(result.getApplicantId());
            assertThat(response.recruitmentTitle()).isEqualTo(result.getRecruitmentTitle());
            assertThat(response.shelterName()).isEqualTo(result.getShelterName());
            assertThat(response.applicantStatus()).isEqualTo(result.getApplicantStatus());
            assertThat(response.recruitmentStartTime()).isEqualTo(result.getRecruitmentStartTime());
            assertThat(response.applicantIsWritedReview())
                .isEqualTo(result.getApplicantIsWritedReview());
        }
    }

    @Nested
    @DisplayName("FindApplicantsResponse resultToResponse 메서드 실행 시")
    class FindApplicantResponseTest {

        @Test
        @DisplayName("성공")
        void resultToResponse() {
            //given
            List<FindApplicantResult> applicantResults
                = ApplicantDtoFixture.findApplicantResults(1);
            Shelter shelter = ShelterFixture.shelter();
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);

            //when
            FindApplicantsResponse findApplicantsResponse
                = ApplicantMapper.resultToResponse(applicantResults, recruitment);

            //then
            FindApplicantResult result = applicantResults.get(0);
            FindApplicantResponse response
                = findApplicantsResponse.applicants().get(0);
            assertThat(response.volunteerId()).isEqualTo(result.getVolunteerId());
            assertThat(response.applicantId()).isEqualTo(result.getApplicantId());
            assertThat(response.volunteerBirthDate()).isEqualTo(result.getVolunteerBirthDate());
            assertThat(response.volunteerGender()).isEqualTo(result.getVolunteerGender());
            assertThat(response.volunteerTemperature()).isEqualTo(result.getVolunteerTemperature());
            assertThat(response.completedVolunteerCount())
                .isEqualTo(result.getCompletedVolunteerCount());
            assertThat(response.applicantStatus())
                .isEqualTo(result.getApplicantStatus().convertToApprovalStatus().name());
        }
    }
}
