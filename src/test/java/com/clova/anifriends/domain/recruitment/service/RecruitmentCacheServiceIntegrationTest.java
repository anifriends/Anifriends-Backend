package com.clova.anifriends.domain.recruitment.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.controller.RecruitmentStatusFilter;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

@Transactional
@Testcontainers
public class RecruitmentCacheServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    RecruitmentCacheService recruitmentCacheService;

    @Autowired
    RecruitmentService recruitmentService;

    @Autowired
    RedisTemplate<String, Long> redisTemplate;

    Shelter shelter;
    String RECRUITMENT_CACHE_KEY;

    @BeforeEach
    void setUp() {
        RECRUITMENT_CACHE_KEY = "recruitment:count";
        shelter = ShelterFixture.shelter();
        shelterRepository.save(shelter);
    }

    @Nested
    @DisplayName("findRecruitmentsV2 실행 시 recruitmentCount를 가져올 때 ")
    class GetRecruitmentCountTest {

        @AfterEach
        void tearDown() {
            redisTemplate.delete(RECRUITMENT_CACHE_KEY);
        }

        @Test
        @DisplayName("성공: redis에 없으면 db에서 가져오고 redis에 있으면 캐시에서 가져온다.")
        void getCachedRecruitmentCount() {
            // given
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            Recruitment savedRecruitment = recruitmentRepository.save(recruitment);

            String keyword = null;
            LocalDate startDate = null;
            LocalDate endDate = null;
            String isClosed = RecruitmentStatusFilter.ALL.getName();
            Boolean title = true;
            Boolean content = true;
            Boolean shelterName = true;
            PageRequest pageRequest = PageRequest.of(0, 10);

            // when
            FindRecruitmentsResponse dbRecruitmentCountResponse = recruitmentService.findRecruitmentsV2(
                keyword, startDate, endDate,
                RecruitmentStatusFilter.valueOf(isClosed).getIsClosed(), title, content,
                shelterName, savedRecruitment.getCreatedAt(),
                savedRecruitment.getRecruitmentId(), pageRequest
            );

            FindRecruitmentsResponse cachedRecruitmentCountResponse = recruitmentService.findRecruitmentsV2(
                keyword, startDate, endDate,
                RecruitmentStatusFilter.valueOf(isClosed).getIsClosed(), title, content,
                shelterName, savedRecruitment.getCreatedAt(),
                savedRecruitment.getRecruitmentId(), pageRequest
            );

            // then
            assertThat(dbRecruitmentCountResponse.pageInfo().totalElements()).isEqualTo(
                cachedRecruitmentCountResponse.pageInfo().totalElements());
        }

        @Test
        @DisplayName("성공: redis에 없으면 -1을 반환한다.")
        void getCachedRecruitmentCountWhenNotExistInRedis() {
            // given
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            recruitmentRepository.save(recruitment);

            // when
            Long recruitmentCount = recruitmentCacheService.getRecruitmentCount();

            // then
            assertThat(recruitmentCount).isEqualTo(-1L);
        }
    }

    @Nested
    @DisplayName("registerRecruitment 실행 시 ")
    class PlusOneToRecruitmentCountTest {

        @Test
        @DisplayName("성공: redis에 값이 없으면 값을 갱신하고 redis에 값이 있으면 count를 하나 증가시킨다.")
        void plusOneToRecruitmentCount() {
            // given
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);

            // when
            recruitmentService.registerRecruitment(
                shelter.getShelterId(),
                recruitment.getTitle(),
                recruitment.getStartTime(),
                recruitment.getEndTime(),
                recruitment.getDeadline(),
                recruitment.getCapacity(),
                recruitment.getContent(),
                recruitment.getImages()
            );

            // then
            assertThat(recruitmentRepository.count()).isEqualTo(
                recruitmentCacheService.getRecruitmentCount());
        }
    }

    @Nested
    @DisplayName("deleteRecruitment 실행 시 ")
    class MinusOneToRecruitmentCountTest {

        @Test
        @DisplayName("성공: redis에 값이 없으면 값을 갱신하고 redis에 값이 있으면 count를 하나 감소시킨다.")
        void MinusOneToRecruitmentCount() {
            // given
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            Recruitment savedRecruitment = recruitmentRepository.save(recruitment);

            // when
            recruitmentService.deleteRecruitment(
                shelter.getShelterId(),
                savedRecruitment.getRecruitmentId()
            );

            // then
            assertThat(recruitmentRepository.count()).isEqualTo(
                recruitmentCacheService.getRecruitmentCount());
        }
    }

}
