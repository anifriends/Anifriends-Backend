package com.clova.anifriends.domain.recruitment.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.controller.RecruitmentStatusFilter;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Transactional
@Testcontainers
class RecruitmentCacheServiceTest extends BaseIntegrationTest {

    @Autowired
    RecruitmentCacheService recruitmentCacheService;

    @Autowired
    RecruitmentService recruitmentService;

    Shelter shelter;


    @Nested
    @DisplayName("findRecruitmentsV2 실행 시 recruitmentCount를 가져올 때 ")
    class findRecruitments {

        @BeforeEach
        void setUp() {
            shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);
        }

        @Test
        @DisplayName("성공: db에 없으면 redis에서 가져온다.")
        void getCachedRecruitmentCount() {
            // given
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            Recruitment savedRecruitment = recruitmentRepository.save(recruitment);

            String keyword = null;
            LocalDate startDate = null;
            LocalDate endDate = null;
            String isClosed = RecruitmentStatusFilter.ALL.getName();
            boolean title = true;
            boolean content = true;
            boolean shelterName = true;
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

    }

}