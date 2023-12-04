package com.clova.anifriends.domain.animal.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.animal.exception.AnimalBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AnimalBreedTest {

    @Nested
    @DisplayName("AnimalBreed 생성 시")
    class NewAnimalBreedTest {

        @Test
        @DisplayName("성공")
        void newAnimalBreed() {
            //given
            String breed = "품종";

            //when
            AnimalBreed animalBreed = new AnimalBreed(breed);

            //then
            assertThat(animalBreed.getBreed()).isEqualTo(breed);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): 입력값이 null")
        void exceptionWhenBreedIsNull() {
            //given
            String nullBreed = null;

            //when
            Exception exception = catchException(() -> new AnimalBreed(nullBreed));

            //then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): 품종이 1자 미만")
        void exceptionWhenLengthLessThan1() {
            //given
            String zero = "";

            //when
            Exception exception = catchException(() -> new AnimalBreed(zero));

            //then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): 품종이 20자 초과")
        void exceptionWhenLengthOver20() {
            //given
            String breedLength20 = "a".repeat(21);

            //when
            Exception exception = catchException(() -> new AnimalBreed(breedLength20));

            //then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }
    }
}
