package com.clova.anifriends.domain.animal.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.animal.exception.AnimalBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AnimalWeightTest {

    @Nested
    @DisplayName("AnimalWeight 생성 시")
    class NewAnimalWeightTest {

        @Test
        @DisplayName("성공")
        void newAnimalWeight() {
            //given
            double weight = 0.7;

            //when
            AnimalWeight animalWeight = new AnimalWeight(weight);

            //then
            assertThat(animalWeight.getWeight()).isEqualTo(weight);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): 몸무게가 0 미만")
        void exceptionWhenWeightLessThan0() {
            //given
            double weightLessThan0 = -1;

            //when
            Exception exception = catchException(() -> new AnimalWeight(weightLessThan0));

            //then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): 몸무게가 99.9 초과")
        void exceptionWhenWeightOver99_9() {
            //given
            double weightOver99_9 = 100;

            //when
            Exception exception = catchException(() -> new AnimalWeight(weightOver99_9));

            //then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }
    }
}
