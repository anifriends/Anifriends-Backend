package com.clova.anifriends.domain.shelter.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ShelterNameTest {

    @Nested
    @DisplayName("ShelterName 생성 시")
    class NewShelterNameTest {

        String name;

        @Test
        @DisplayName("성공")
        void newShelterName() {
            // given
            name = "홍길동";

            // when
            ShelterName shelterName = new ShelterName(name);

            // then
            assertThat(shelterName.getName()).isEqualTo(name);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 이름이 null인 경우")
        void throwExceptionWhenNameIsNull() {
            // given
            name = null;

            // when
            Exception exception = catchException(
                () -> new ShelterName(name));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 이름이 blank인 경우")
        void throwExceptionWhenNameIsBlank() {
            // given
            name = " ";

            // when
            Exception exception = catchException(
                () -> new ShelterName(name));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외: 이름이 20자 초과인 경우")
        void throwExceptionWhenNameIsMoreThanTen() {
            // given
            name = "abcdefghijkfasdfwerxfsfaserawe";

            // when
            Exception exception = catchException(
                () -> new ShelterName(name));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }
    }

    @Nested
    @DisplayName("update 실행 시")
    class Update {

        @Test
        @DisplayName("성공: 이름의 길이가 20")
        void updateWhenLengthIs20() {
            // given
            String value = "a".repeat(20);

            // when
            Exception exception = catchException(() -> new ShelterName(value));

            // then
            assertThat(exception).isNull();

        }

        @Test
        @DisplayName("성공: 이름의 길이가 1")
        void updateWhenLengthIs1() {
            // given
            String value = "a".repeat(1);

            // when
            Exception exception = catchException(() -> new ShelterName(value));

            // then
            assertThat(exception).isNull();

        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 이름이 null")
        void exceptionWhenNameIsNull() {
            // given
            String nullValue = null;

            // when
            Exception exception = catchException(() -> new ShelterName(nullValue));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);

        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 이름이 빈 문자열")
        void exceptionWhenNameIsBlank() {
            // given
            String blankValue = "";

            // when
            Exception exception = catchException(() -> new ShelterName(blankValue));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);

        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 이름의 길이가 20 초과")
        void exceptionWhenNameLengthOver20() {
            // given
            String value = "a".repeat(21);

            // when
            Exception exception = catchException(() -> new ShelterName(value));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);

        }
    }
}
