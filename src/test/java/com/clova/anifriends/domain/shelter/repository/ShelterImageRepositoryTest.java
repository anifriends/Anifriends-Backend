package com.clova.anifriends.domain.shelter.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.ShelterImage;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.shelter.support.ShelterImageFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ShelterImageRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private ShelterImageRepository shelterImageRepository;

    Shelter givenShelter;
    ShelterImage givenShelterImage;

    @BeforeEach
    void setUp() {
        givenShelter = ShelterFixture.shelter();
        givenShelterImage = ShelterImageFixture.shelterImage(givenShelter);
    }

    @Nested
    @DisplayName("findShelterDetail 실행 시")
    class findShelterImageByShelterTest {

        @Test
        @DisplayName("성공")
        public void success() {
            // given
            shelterRepository.save(givenShelter);
            shelterImageRepository.save(givenShelterImage);

            // when
            ShelterImage foundShelterImage = shelterImageRepository.findShelterImageByShelter(
                givenShelter).get();

            // then
            assertThat(foundShelterImage.getImageUrl()).isEqualTo(givenShelterImage.getImageUrl());
        }
    }
}
