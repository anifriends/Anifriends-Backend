package com.clova.anifriends.domain.animal.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.exception.AnimalBadRequestException;
import com.clova.anifriends.domain.animal.support.fixture.AnimalFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AnimalAdoptedTest {

    @Nested
    @DisplayName("updateAdoptStatus 실행 시")
    class UpdateAdoptStatus {

        @Test
        @DisplayName("성공: false -> true")
        void updateAdoptStatusFalseToTrue() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            boolean originStatus = false;
            boolean updateStatus = true;
            Animal animal = AnimalFixture.animal(shelter, originStatus);

            // when
            animal.updateAdoptStatus(updateStatus);

            // then
            assertThat(animal.isAdopted()).isEqualTo(updateStatus);
        }

        @Test
        @DisplayName("성공: true -> false")
        void updateAdoptStatusTrueToFalse() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            boolean originStatus = true;
            boolean updateStatus = false;
            Animal animal = AnimalFixture.animal(shelter, originStatus);

            // when
            animal.updateAdoptStatus(updateStatus);

            // then
            assertThat(animal.isAdopted()).isEqualTo(updateStatus);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): false -> false")
        void exceptionWhenFalseToFalse() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            boolean originStatus = false;
            boolean updateStatus = false;
            Animal animal = AnimalFixture.animal(shelter, originStatus);

            // when
            Exception exception = catchException(() -> animal.updateAdoptStatus(updateStatus));

            // then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): true -> true")
        void exceptionWhenTrueToTrue() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            boolean originStatus = true;
            boolean updateStatus = true;
            Animal animal = AnimalFixture.animal(shelter, originStatus);

            // when
            Exception exception = catchException(() -> animal.updateAdoptStatus(updateStatus));

            // then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }
    }

}
