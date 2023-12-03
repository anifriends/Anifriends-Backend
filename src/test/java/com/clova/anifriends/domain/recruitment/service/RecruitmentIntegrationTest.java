package com.clova.anifriends.domain.recruitment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.RecruitmentImage;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.recruitment.vo.RecruitmentInfo;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

public class RecruitmentIntegrationTest extends BaseIntegrationTest {

    @Autowired
    RecruitmentService recruitmentService;

    @Autowired
    ShelterRepository shelterRepository;

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Nested
    @DisplayName("updateRecruitment 메서드 호출 시")
    class UpdateRecruitmentTest {

        Shelter shelter;
        Recruitment recruitment;

        @BeforeEach
        void setUp() {
            shelter = ShelterFixture.shelter();
            recruitment = RecruitmentFixture.recruitment(shelter);
            shelterRepository.save(shelter);
            recruitmentRepository.save(recruitment);
        }

        @Test
        @DisplayName("성공: 이미지 url 입력값이 현재 이미지와 같을 때, 새로운 엔티티는 생성되지 않는다.")
        void updateRecruitmentWhenNoMoreEntities() {
            //given
            List<String> givenImageUrls = recruitment.getImages();

            //when
            recruitmentService.updateRecruitment(shelter.getShelterId(),
                recruitment.getRecruitmentId(), null, null, null,
                null, null, null, givenImageUrls);

            //then
            List<RecruitmentImage> recruitmentImages = entityManager.createQuery(
                    "select ri from RecruitmentImage ri", RecruitmentImage.class)
                .getResultList();
            assertThat(recruitmentImages).hasSize(givenImageUrls.size());
        }
    }

    @Nested
    @DisplayName("deleteRecruitment 메서드 호출 시")
    class DeleteRecruitmentTest {

        Shelter shelter;
        Recruitment recruitment;

        @BeforeEach
        void setUp() {
            shelter = ShelterFixture.shelter();
            recruitment = RecruitmentFixture.recruitment(shelter);
            shelterRepository.save(shelter);
        }

        @Test
        @DisplayName("성공")
        void deleteRecruitment() {
            //given
            List<String> imageUrls = List.of("image1", "image2");
            recruitment.updateRecruitment(null, null, null, null, null, null, imageUrls);
            recruitmentRepository.save(recruitment);

            //when
            recruitmentService.deleteRecruitment(shelter.getShelterId(),
                recruitment.getRecruitmentId());

            //then
            verify(s3Service, times(1)).deleteImages(imageUrls);
            Recruitment findRecruitment = entityManager.find(Recruitment.class,
                recruitment.getRecruitmentId());
            assertThat(findRecruitment).isNull();
            List<RecruitmentImage> recruitmentImages = entityManager.createQuery(
                    "select ri from RecruitmentImage ri", RecruitmentImage.class)
                .getResultList();
            assertThat(recruitmentImages).isEmpty();
        }

        @Test
        @DisplayName("성공: 신청자가 있는 경우")
        void deleteRecruitmentWhenExistsApplicants() {
            //given
            Volunteer volunteer = VolunteerFixture.volunteer();
            volunteerRepository.save(volunteer);
            recruitmentRepository.save(recruitment);
            Applicant applicant = new Applicant(recruitment, volunteer);
            applicantRepository.save(applicant);

            //when
            recruitmentService.deleteRecruitment(shelter.getShelterId(),
                recruitment.getRecruitmentId());

            //then
            Recruitment findRecruitment = entityManager.find(Recruitment.class,
                recruitment.getRecruitmentId());
            assertThat(findRecruitment).isNull();
        }
    }

    @Nested
    @DisplayName("autoCloseRecruitment 메서드 호출 시")
    class AutoCloseRecruitmentTest {

        Shelter shelter;

        @BeforeEach
        void setUp() {
            shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);
        }

        @Test
        @DisplayName("성공: 저장소 업데이트 됨")
        void autoCloseRecruitment() {
            //given
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            RecruitmentInfo recruitmentInfo = new RecruitmentInfo(recruitment.getStartTime(),
                recruitment.getEndTime(), recruitment.getDeadline(), recruitment.isClosed(),
                recruitment.getCapacity());
            LocalDateTime deadlineBeforeNow = LocalDateTime.now().minusDays(1);
            ReflectionTestUtils.setField(recruitmentInfo, "deadline", deadlineBeforeNow);
            ReflectionTestUtils.setField(recruitment, "info", recruitmentInfo);
            recruitmentRepository.save(recruitment);

            //when
            recruitmentService.autoCloseRecruitment();

            //then
            Recruitment findRecruitment = entityManager.find(Recruitment.class,
                recruitment.getRecruitmentId());
            assertThat(findRecruitment.isClosed()).isTrue();
        }
    }
}
