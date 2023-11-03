package com.clova.anifriends.domain.applicant.repository;

import static com.clova.anifriends.domain.applicant.support.ApplicantFixture.applicant;
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
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ApplicantRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Nested
    @DisplayName("findByApplicantIdAndVolunteerId 실행 시")
    class FindByApplicantIdAndVolunteerIdTest {

        @Test
        @DisplayName("성공")
        void findByApplicantIdAndVolunteerId() {
            // given
            Shelter shelter = shelter();
            Volunteer volunteer = volunteer();
            Recruitment recruitment = recruitment(shelter);
            Applicant applicant = applicant(recruitment, volunteer);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            recruitmentRepository.save(recruitment);
            applicantRepository.save(applicant);

            // when
            Optional<Applicant> result = applicantRepository
                .findByApplicantIdAndVolunteerId(applicant.getApplicantId(),
                    volunteer.getVolunteerId());

            // then
            assertThat(result).isEqualTo(Optional.of(applicant));
        }
    }


}