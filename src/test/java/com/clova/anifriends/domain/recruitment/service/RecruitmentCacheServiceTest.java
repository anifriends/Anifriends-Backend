package com.clova.anifriends.domain.recruitment.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse.FindRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

class RecruitmentCacheServiceTest extends BaseIntegrationTest {

    @Autowired
    RecruitmentCacheService recruitmentCacheService;

    @Nested
    class RegisterRecruitmentTest {

        Shelter shelter;

        @BeforeEach
        void setUp() {
            shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);
        }

        @Test
        void registerRecruitment() {
            //given
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);

            //when
            recruitmentCacheService.registerRecruitment(recruitment);

            //then
            List<FindRecruitmentResponse> recruitments = recruitmentCacheService.findRecruitments();
            assertThat(recruitments).hasSize(1);
        }

        @Test
        void overflowTest() {
            //given
            List<Recruitment> list = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                Recruitment recruitmented = RecruitmentFixture.recruitment(shelter);
                recruitmentRepository.save(recruitmented);
                list.add(recruitmented);
            }
            int i = 1;
            for (Recruitment recruitment : list) {
                ReflectionTestUtils.setField(recruitment, "createdAt", LocalDateTime.now().plusHours(i));
                i++;
            }

            //when
            for (Recruitment recruitment : list) {
                recruitmentCacheService.registerRecruitment(recruitment);
            }

            //then
            List<FindRecruitmentResponse> recruitments = recruitmentCacheService.findRecruitments();
            assertThat(recruitments).hasSize(20);
            recruitments.forEach(recruitment -> {
                System.out.println(recruitment);
            });
        }


    }
}