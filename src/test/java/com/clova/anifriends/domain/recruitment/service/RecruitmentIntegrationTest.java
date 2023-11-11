package com.clova.anifriends.domain.recruitment.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.RecruitmentImage;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
            List<String> givenImageUrls = recruitment.getImageUrls();

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
}
