package com.clova.anifriends.domain.recruitment.repository;

import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class RecruitmentRepositoryImplTest extends BaseRepositoryTest {

    @Autowired
    private RecruitmentRepositoryImpl customRecruitmentRepository;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Nested
    @DisplayName("findRecruitmentsByShelterOrderByCreatedAt 메서드 실행 시")
    class FindRecruitmentsByShelterOrderByCreatedAtTest {

        @Test
        @DisplayName("성공: 모든 인자가 주어졌을 때")
        void findRecruitmentsWhenArgsAreNotNull() {
            // given
            Shelter shelter = shelter();
            setField(shelter, "shelterId", 1L);

            shelterRepository.save(shelter);

            Recruitment recruitment1 = new Recruitment(
                shelter,
                "a",
                10,
                "d",
                LocalDateTime.now().plusMonths(2),
                LocalDateTime.now().plusMonths(2).plusHours(3),
                LocalDateTime.now().plusDays(1),
                List.of()
            );

            Recruitment recruitment2 = new Recruitment(
                shelter,
                "ab",
                10,
                "de",
                LocalDateTime.now().plusMonths(3),
                LocalDateTime.now().plusMonths(3).plusHours(3),
                LocalDateTime.now().plusMonths(1),
                List.of()
            );

            setField(recruitment2.getInfo(), "isClosed", true);

            Recruitment recruitment3 = new Recruitment(
                shelter,
                "abc",
                10,
                "def",
                LocalDateTime.now().plusMonths(4),
                LocalDateTime.now().plusMonths(4).plusHours(3),
                LocalDateTime.now().plusMonths(2),
                List.of()
            );

            recruitmentRepository.saveAll(List.of(recruitment1, recruitment2, recruitment3));

            String keyword = "ab";
            LocalDate startDate = LocalDate.now().plusMonths(3);
            LocalDate endDate = LocalDate.now().plusMonths(5);
            boolean content = true;
            boolean title = true;
            PageRequest pageable = PageRequest.of(0, 10);

            // when
            Page<Recruitment> recruitments = customRecruitmentRepository.findRecruitmentsByShelterOrderByCreatedAt(
                shelter.getShelterId(),
                keyword,
                startDate,
                endDate,
                content,
                title,
                pageable
            );

            // then
            assertThat(recruitments).contains(recruitment2, recruitment3);
        }

        @Test
        @DisplayName("성공: 모든 인자가 null")
        void findRecruitmentsWhenArgsAreNull() {
            // given
            Shelter shelter = shelter();
            setField(shelter, "shelterId", 1L);

            shelterRepository.save(shelter);

            Recruitment recruitment1 = new Recruitment(
                shelter,
                "a",
                10,
                "d",
                LocalDateTime.now().plusMonths(2),
                LocalDateTime.now().plusMonths(2).plusHours(3),
                LocalDateTime.now().plusDays(1),
                List.of()
            );

            Recruitment recruitment2 = new Recruitment(
                shelter,
                "ab",
                10,
                "de",
                LocalDateTime.now().plusMonths(3),
                LocalDateTime.now().plusMonths(3).plusHours(3),
                LocalDateTime.now().plusMonths(1),
                List.of()
            );

            setField(recruitment2.getInfo(), "isClosed", true);

            Recruitment recruitment3 = new Recruitment(
                shelter,
                "abc",
                10,
                "def",
                LocalDateTime.now().plusMonths(4),
                LocalDateTime.now().plusMonths(4).plusHours(3),
                LocalDateTime.now().plusMonths(2),
                List.of()
            );

            recruitmentRepository.saveAll(List.of(recruitment1, recruitment2, recruitment3));

            PageRequest pageable = PageRequest.of(0, 10);

            // when
            Page<Recruitment> recruitments = customRecruitmentRepository.findRecruitmentsByShelterOrderByCreatedAt(
                shelter.getShelterId(),
                null,
                null,
                null,
                null,
                null,
                pageable
            );

            // then
            assertThat(recruitments).contains(recruitment1, recruitment2, recruitment3);
        }
    }


}