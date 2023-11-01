package com.clova.anifriends.domain.shelter.wrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ShelterNameTest {

    @Nested
    @DisplayName("VolunteerName 생성 시")
    class newShelterNameTest {

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
        @DisplayName("예외: 이름이 10자 초과인 경우")
        void throwExceptionWhenNameIsMoreThanTen() {
            // given
            name = "abcdefghijk";

            // when
            Exception exception = catchException(
                () -> new ShelterName(name));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }
    }
}
