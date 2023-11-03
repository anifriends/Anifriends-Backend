package com.clova.anifriends.domain.applicant.repository;

import static com.clova.anifriends.domain.applicant.support.ApplicantFixture.applicantWithStatus;
import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.NO_SHOW;
import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.PENDING;
import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.REFUSED;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static com.clova.anifriends.domain.volunteer.support.VolunteerFixture.volunteer;
import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ApplicantRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Nested
    @DisplayName("findApprovedByRecruitmentIdAndShelterId 메서드 실행 시")
    class FindApprovedByRecruitmentIdAndShelterId {

        @Test
        @DisplayName("성공: 승인된(출석, 노쇼) 봉사 신청 조회 2명")
        void findApprovedByRecruitmentIdAndShelterId1() {
            // given
            Shelter shelter = shelter();
            Volunteer volunteer = volunteer();
            Recruitment recruitment = recruitment(shelter);
            Applicant applicantAttendance = applicantWithStatus(recruitment, volunteer, ATTENDANCE);
            Applicant applicantNoShow = applicantWithStatus(recruitment, volunteer, NO_SHOW);
            Applicant applicantPending = applicantWithStatus(recruitment, volunteer, PENDING);
            Applicant applicantRefused = applicantWithStatus(recruitment, volunteer, REFUSED);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            recruitmentRepository.save(recruitment);
            applicantRepository.saveAll(
                List.of(applicantAttendance, applicantNoShow, applicantPending, applicantRefused)
            );
            List<Applicant> expected = List.of(applicantAttendance, applicantNoShow);

            // when
            List<Applicant> result = applicantRepository
                .findApprovedByRecruitmentIdAndShelterId(recruitment.getRecruitmentId(),
                    shelter.getShelterId());

            // then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("성공: 승인된(출석, 노쇼) 봉사 신청 조회 0 명")
        void findApprovedByRecruitmentIdAndShelterId2() {
            // given
            Shelter shelter = shelter();
            Volunteer volunteer = volunteer();
            Recruitment recruitment = recruitment(shelter);
            Applicant applicantPending = applicantWithStatus(recruitment, volunteer, PENDING);
            Applicant applicantRefused = applicantWithStatus(recruitment, volunteer, REFUSED);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            recruitmentRepository.save(recruitment);
            applicantRepository.saveAll(
                List.of(applicantPending, applicantRefused)
            );
            List<Applicant> expected = List.of();

            // when
            List<Applicant> result = applicantRepository
                .findApprovedByRecruitmentIdAndShelterId(recruitment.getRecruitmentId(),
                    shelter.getShelterId());

            // then
            assertThat(result).isEqualTo(expected);
        }
    }

}