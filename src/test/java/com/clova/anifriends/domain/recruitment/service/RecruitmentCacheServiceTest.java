package com.clova.anifriends.domain.recruitment.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse.FindRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class RecruitmentCacheServiceTest extends BaseIntegrationTest {

    private static final String RECRUITMENT_KEY = "recruitment";
    private static final int ZERO = 0;
    private static final int ALL_ELEMENT = -1;

    @Autowired
    RecruitmentCacheService recruitmentCacheService;

    @Autowired
    RedisTemplate<String, FindRecruitmentResponse> redisTemplate;

    @AfterEach
    void tearDown() {
        redisTemplate.opsForZSet()
            .removeRange(RECRUITMENT_KEY, ZERO, ALL_ELEMENT);
    }

    @Nested
    @DisplayName("pushNewRecruitment 메서드 호출 시")
    class PushNewRecruitment {

        Shelter shelter;

        @BeforeEach
        void setUp() {
            shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);
        }

        @Test
        @DisplayName("성공: 새로운 Recruitment 캐싱")
        void registerRecruitment() {
            //given
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            recruitmentRepository.save(recruitment);

            //when
            recruitmentCacheService.pushNewRecruitment(recruitment);

            //then
            List<FindRecruitmentResponse> recruitments = recruitmentCacheService.findRecruitments();
            assertThat(recruitments).hasSize(1);
            FindRecruitmentResponse recruitmentResponse = recruitments.get(0);
            assertThat(recruitmentResponse.recruitmentId())
                .isEqualTo(recruitment.getRecruitmentId());
        }

        @Test
        @DisplayName("성공: 새로운 Recruitment 100개 생성, 역순 20개 캐싱")
        void overflowTest() {
            //given
            List<Recruitment> recruitments = IntStream.range(0, 100)
                .mapToObj(i -> RecruitmentFixture.recruitment(shelter))
                .toList();
            recruitmentRepository.saveAll(recruitments);
            LocalDateTime now = LocalDateTime.now();
            int hour = 0;
            for (Recruitment recruitment : recruitments) {
                ReflectionTestUtils.setField(recruitment, "createdAt", now.plusHours(hour++));
            }

            //when
            for (Recruitment recruitment : recruitments) {
                recruitmentCacheService.pushNewRecruitment(recruitment);
            }

            //then
            List<Long> recruitmentIdsDesc = recruitments.stream()
                .map(Recruitment::getRecruitmentId)
                .filter(i -> i > 80)
                .sorted(Comparator.reverseOrder())
                .toList();

            List<FindRecruitmentResponse> findRecruitments
                = recruitmentCacheService.findRecruitments();
            assertThat(findRecruitments).hasSize(20);
            assertThat(findRecruitments).map(FindRecruitmentResponse::recruitmentId)
                .containsExactlyElementsOf(recruitmentIdsDesc);
        }
    }
}