package com.clova.anifriends.domain.animal.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.animal.exception.AnimalBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AnimalNameTest {

    @Nested
    @DisplayName("AnimalName 생성 시")
    class NewAnimalNameTest {

        @Test
        @DisplayName("성공")
        void newAnimalName() {
            //given
            String name = "name";

            //when
            AnimalName animalName = new AnimalName(name);

            //then
            assertThat(animalName.getName()).isEqualTo(name);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): 입력값이 null")
        void exceptionWhenNameIsNull() {
            //given
            String nullName = null;

            //when
            Exception exception = catchException(() -> new AnimalName(nullName));

            //then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): 이름이 1자 미만")
        void exceptionWhenNameIsZero() {
            //given
            String zero = "";

            //when
            Exception exception = catchException(() -> new AnimalName(zero));

            //then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): 이름이 20자 초과")
        void exceptionWhenNameOver20() {
            //given
            String nameLength20 = "a".repeat(21);

            //when
            Exception exception = catchException(() -> new AnimalName(nameLength20));

            //then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }
    }
}
