package com.clova.anifriends.domain.recruitment.repository;

import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RecruitmentRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Test
    @DisplayName("findByShelterIdAndRecruitmentId 실행 시")
    void findByShelterIdAndRecruitmentId() {
        // given
        Shelter shelter = shelter();
        Recruitment recruitment = recruitment(shelter);

        shelterRepository.save(shelter);
        recruitmentRepository.save(recruitment);

        // when
        Optional<Recruitment> foundRecruitment = recruitmentRepository.findByShelterIdAndRecruitmentId(
            shelter.getShelterId(), recruitment.getRecruitmentId());

        // then
        assertThat(foundRecruitment).isEqualTo(Optional.of(recruitment));
    }

}