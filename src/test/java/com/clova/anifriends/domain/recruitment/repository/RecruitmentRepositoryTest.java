package com.clova.anifriends.domain.recruitment.repository;

import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.ATTENDANCE;
import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

class RecruitmentRepositoryTest extends BaseRepositoryTest {

    @Nested
    @DisplayName("findCompletedRecruitments 메서드 실행 시")
    class FindCompletedRecruitmentsTest {

        @BeforeEach
        void setUp() {
            List<Shelter> shelters = ShelterFixture.createShelters(20);
            List<Recruitment> recruitments = RecruitmentFixture.createRecruitments(shelters);
            shelters.forEach(shelter -> entityManager.persist(shelter));
            recruitments.forEach(recruitment -> entityManager.persist(recruitment));
        }

        @Test
        @DisplayName("성공")
        void findCompletedRecruitments() {
            //given
            Shelter shelter = ShelterFixture.shelter();
            Recruitment expected = RecruitmentFixture.recruitment(shelter);
            Volunteer volunteer = VolunteerFixture.volunteer();
            entityManager.persist(shelter);
            entityManager.persist(expected);
            entityManager.persist(volunteer);
            Applicant applicant = new Applicant(expected, volunteer);
            ReflectionTestUtils.setField(applicant, "status", ATTENDANCE);
            entityManager.persist(applicant);
            PageRequest pageRequest = PageRequest.of(0, 10);

            //when
            Page<Recruitment> recruitments = recruitmentRepository.findCompletedRecruitments(
                volunteer.getVolunteerId(), pageRequest);

            //then
            assertThat(recruitments).hasSize(1);
            Recruitment recruitment = recruitments.getContent().get(0);
            assertThat(recruitment).isEqualTo(expected);
        }
    }
}
