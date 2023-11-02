package com.clova.anifriends.domain.recruitment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class RecruitmentRepositoryImplTest extends BaseRepositoryTest {

    @Nested
    @DisplayName("findRecruitments 메서드 실행 시")
    class FindRecruitmentsTest {

        //todo: 다양한 케이스에 대한 테스트를 작성할 것
        @Test
        @DisplayName("성공: 모든 인자가 null")
        void findRecruitmentsWhenArgsAreNull() {
            //given
            Shelter shelter = ShelterFixture.shelter();
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            PageRequest pageRequest = PageRequest.of(0, 10);
            shelterRepository.save(shelter);
            recruitmentRepository.save(recruitment);

            //when
            Page<Recruitment> recruitments = recruitmentRepository.findRecruitments(null, null,
                null, null, false, false, false,
                pageRequest);

            //then
            assertThat(recruitments.getTotalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("성공: 모든 인자가 주어졌을 때")
        void findRecruitmentsWhenArgsAreNotNull() {
            //given
            Shelter shelter = ShelterFixture.shelter();
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            PageRequest pageRequest = PageRequest.of(0, 10);
            String keyword = shelter.getName();
            LocalDate dateCondition = recruitment.getStartTime().toLocalDate();
            boolean isClosed = false;
            boolean titleFilter = true;
            boolean contentFilter = true;
            boolean shelterNameFilter = true;
            shelterRepository.save(shelter);
            recruitmentRepository.save(recruitment);

            //when
            Page<Recruitment> recruitments = recruitmentRepository.findRecruitments(keyword,
                dateCondition, dateCondition, isClosed, titleFilter, contentFilter,
                shelterNameFilter, pageRequest);

            //then
            assertThat(recruitments.getTotalElements()).isEqualTo(1);
            Recruitment findRecruitment = recruitments.getContent().get(0);
            assertThat(findRecruitment.getTitle()).isEqualTo(recruitment.getTitle());
        }
    }
}