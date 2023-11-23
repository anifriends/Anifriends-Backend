package com.clova.anifriends.domain.applicant.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.exception.ApplicantConflictException;
import com.clova.anifriends.domain.applicant.support.ApplicantFixture;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ApplicantIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ApplicantService applicantService;

    @Nested
    @DisplayName("registerApplicant 메서드 호출 시")
    class RegisterApplicantTest {

        Shelter shelter;

        @BeforeEach
        void setUp() {
            shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);
        }

        @Test
        @DisplayName("예외(ApplicantConflictException): 중복 봉사 신청")
        void exceptionWhenDuplicateApply() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            Applicant applicant = ApplicantFixture.applicant(recruitment, volunteer);

            volunteerRepository.save(volunteer);
            recruitmentRepository.save(recruitment);
            applicantRepository.save(applicant);

            // when
            Exception exception = catchException(
                () -> applicantService.registerApplicant(recruitment.getRecruitmentId(),
                    volunteer.getVolunteerId()));

            // then
            assertThat(exception).isInstanceOf(ApplicantConflictException.class);
        }

        @Test
        @DisplayName("성공: 30명 정원, 30명 동시 신청")
        void registerApplicantWhenRegisterWith30In30Capacity() throws InterruptedException {
            //gvien
            int capacity = 30;
            List<Volunteer> volunteers = VolunteerFixture.volunteers(capacity);
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter, capacity);
            volunteerRepository.saveAll(volunteers);
            recruitmentRepository.save(recruitment);

            int poolSize = 30;
            ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
            CountDownLatch latch = new CountDownLatch(poolSize);

            //when
            for (int i = 0; i < poolSize; i++) {
                int finalI = i;
                executorService.submit(() -> {
                    try {
                        applicantService.registerApplicant(shelter.getShelterId(),
                            volunteers.get(finalI).getVolunteerId());
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            //then
            Recruitment findRecruitment = entityManager.find(Recruitment.class,
                recruitment.getRecruitmentId());
            List<Applicant> findApplicants = getApplicants(recruitment);
            assertThat(findRecruitment.getApplicantCount()).isEqualTo(capacity);
            assertThat(findApplicants).hasSize(capacity);
        }

        @Test
        @DisplayName("성공: 10명 정원, 30명 동시 신청")
        void registerApplicantWhenRegisterWith30In10Capacity() throws InterruptedException {
            //gvien
            int capacity = 10;
            List<Volunteer> volunteers = VolunteerFixture.volunteers(capacity);
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter, capacity);
            volunteerRepository.saveAll(volunteers);
            recruitmentRepository.save(recruitment);

            int poolSize = 30;
            ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
            CountDownLatch latch = new CountDownLatch(poolSize);

            //when
            for (int i = 0; i < poolSize; i++) {
                int finalI = i;
                executorService.submit(() -> {
                    try {
                        applicantService.registerApplicant(shelter.getShelterId(),
                            volunteers.get(finalI).getVolunteerId());
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            //then
            Recruitment findRecruitment = entityManager.find(Recruitment.class,
                recruitment.getRecruitmentId());
            List<Applicant> findApplicants = getApplicants(recruitment);
            assertThat(findRecruitment.getApplicantCount()).isEqualTo(capacity);
            assertThat(findApplicants).hasSize(capacity);
        }

        private List<Applicant> getApplicants(Recruitment recruitment) {
            return entityManager.createQuery(
                    "select a from Applicant a where a.recruitment.recruitmentId = :recruitmentId",
                    Applicant.class)
                .setParameter("recruitmentId", recruitment.getRecruitmentId())
                .getResultList();
        }
    }
}
