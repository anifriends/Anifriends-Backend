package com.clova.anifriends.domain.volunteer.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VolunteerNameTest {

    @Nested
    @DisplayName("VolunteerName 생성 시")
    class newVolunteerNameTest {

        String name;

        @Test
        @DisplayName("성공")
        void success() {
            // given
            name = "홍길동";

            // when
            VolunteerName volunteerName = new VolunteerName(name);

            // then
            assertThat(volunteerName.getName()).isEqualTo(name);
        }

        @Test
        @DisplayName("예외: 이름이 null인 경우")
        void throwExceptionWhenNameIsNull() {
            // given
            name = null;

            // when
            // then
            assertThatThrownBy(() -> new VolunteerName(name))
                .isInstanceOf(VolunteerBadRequestException.class);
        }

        @Test
        @DisplayName("예외: 이름이 blank인 경우")
        void throwExceptionWhenNameIsBlank() {
            // given
            name = " ";

            // when
            // then
            assertThatThrownBy(() -> new VolunteerName(name))
                .isInstanceOf(VolunteerBadRequestException.class);
        }

        @Test
        @DisplayName("예외: 이름이 10자 초과인 경우")
        void throwExceptionWhenNameIsMoreThanTen() {
            // given
            name = "abcdefghijk";

            // when
            // then
            assertThatThrownBy(() -> new VolunteerName(name))
                .isInstanceOf(VolunteerBadRequestException.class);
        }
    }
}