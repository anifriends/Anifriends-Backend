package com.clova.anifriends.domain.animal.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.animal.exception.AnimalBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AnimalInformationTest {

    @Nested
    @DisplayName("AnimalInformation 생성 시")
    class NewAnimalInformationTest {

        @Test
        @DisplayName("성공")
        void newAnimalInformation() {
            //given
            String information = "기타 정보";

            //when
            AnimalInformation animalInformation = new AnimalInformation(information);

            //then
            assertThat(animalInformation.getInformation()).isEqualTo(information);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): 입력값이 null")
        void exceptionWhenInformationIsNull() {
            //given
            String nullInfo = null;

            //when
            Exception exception = catchException(() -> new AnimalInformation(nullInfo));

            //then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): 기타 정보가 1자 미만")
        void exceptionWhenInformationLessThan1() {
            //given
            String infoLessThan1 = "";

            //when
            Exception exception = catchException(() -> new AnimalInformation(infoLessThan1));

            //then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): 입력값이 null")
        void exceptionWhenInformationOver1000() {
            //given
            String infoOver1000 = "a".repeat(1001);

            //when
            Exception exception = catchException(() -> new AnimalInformation(infoOver1000));

            //then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }
    }
}
