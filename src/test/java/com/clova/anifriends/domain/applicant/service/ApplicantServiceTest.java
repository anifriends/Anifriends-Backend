package com.clova.anifriends.domain.applicant.service;

import static com.clova.anifriends.domain.applicant.support.ApplicantFixture.applicant;
import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static com.clova.anifriends.domain.volunteer.support.VolunteerFixture.volunteer;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.dto.FindApplicantsApprovedResponse;
import com.clova.anifriends.domain.applicant.exception.ApplicantConflictException;
import com.clova.anifriends.domain.applicant.repository.ApplicantRepository;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.recruitment.wrapper.RecruitmentInfo;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApplicantServiceTest {

    @InjectMocks
    ApplicantService applicantService;

    @Mock
    ApplicantRepository applicantRepository;

    @Mock
    RecruitmentRepository recruitmentRepository;

    @Mock
    VolunteerRepository volunteerRepository;

    @Nested
    @DisplayName("registerApplicant 메서드 실행 시")
    class RegisterApplicantTest {

        Shelter shelter = shelter();
        Volunteer volunteer = volunteer();
        Recruitment recruitment = recruitment(shelter);
        RecruitmentInfo recruitmentInfo = new RecruitmentInfo(
            LocalDateTime.now().plusDays(5),
            LocalDateTime.now().plusDays(10),
            LocalDateTime.now().plusDays(3),
            false,
            30
        );


        @Test
        @DisplayName("성공")
        void registerApplicant() {
            // given
            setField(volunteer, "volunteerId", 1L);
            setField(recruitment, "recruitmentId", 1L);
            setField(recruitment, "info", recruitmentInfo);
            given(recruitmentRepository.findById(anyLong())).willReturn(
                Optional.ofNullable(recruitment));
            given(volunteerRepository.findById(anyLong())).willReturn(
                Optional.ofNullable(volunteer));
            given(applicantRepository.existsByRecruitmentAndVolunteer(recruitment, volunteer))
                .willReturn(false);

            // when
            applicantService.registerApplicant(recruitment.getRecruitmentId(),
                volunteer.getVolunteerId());

            // then
            then(applicantRepository).should().save(any());
        }

        @Test
        @DisplayName("예외(ApplicantConflictException): 이미 신청한 경우")
        void throwExceptionWhenAlreadyApplied() {
            // given
            setField(volunteer, "volunteerId", 1L);
            setField(recruitment, "recruitmentId", 1L);
            setField(recruitment, "info", recruitmentInfo);
            given(recruitmentRepository.findById(anyLong())).willReturn(
                Optional.ofNullable(recruitment));
            given(volunteerRepository.findById(anyLong())).willReturn(
                Optional.ofNullable(volunteer));
            when(applicantRepository.existsByRecruitmentAndVolunteer(recruitment, volunteer))
                .thenReturn(true);

            // when
            Exception exception = catchException(() -> applicantService.registerApplicant(
                recruitment.getRecruitmentId(), volunteer.getVolunteerId()));

            // then
            assertThat(exception).isInstanceOf(ApplicantConflictException.class);
        }
    }

    @Nested
    @DisplayName("findApplicantsApproved 메서드 실행 시")
    class FindApplicantsApprovedTest {

        @Test
        @DisplayName("성공: 봉사 승인자가 1명 이상인 경우")
        void findApplicantsApproved1() {
            // given
            Recruitment recruitment = recruitment(shelter());
            Volunteer volunteer = volunteer();
            Applicant applicantApproved = applicant(recruitment, volunteer, ATTENDANCE);

            FindApplicantsApprovedResponse response = FindApplicantsApprovedResponse.from(
                List.of(applicantApproved));

            when(applicantRepository.findApprovedByRecruitmentIdAndShelterId(anyLong(), anyLong()))
                .thenReturn(List.of(applicantApproved));

            // when
            FindApplicantsApprovedResponse result = applicantService.findApplicantsApproved(
                anyLong(), anyLong());

            // then
            assertThat(result).isEqualTo(response);
        }

        @Test
        @DisplayName("성공: 봉사 승인자가 0 명인 경우")
        void findApplicantsApproved2() {
            // given
            FindApplicantsApprovedResponse response = FindApplicantsApprovedResponse.from(
                List.of());

            when(applicantRepository.findApprovedByRecruitmentIdAndShelterId(anyLong(), anyLong()))
                .thenReturn(List.of());

            // when
            FindApplicantsApprovedResponse result = applicantService.findApplicantsApproved(
                anyLong(), anyLong());

            // then
            assertThat(result).isEqualTo(response);
        }
    }
}
