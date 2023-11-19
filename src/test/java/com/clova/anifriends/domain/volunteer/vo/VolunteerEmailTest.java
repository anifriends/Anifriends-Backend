package com.clova.anifriends.domain.volunteer.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VolunteerEmailTest {

    @Nested
    @DisplayName("VolunteerEmail 생성 시")
    class newVolunteerEmailTest {

        String email;

        @Test
        @DisplayName("성공")
        void success() {
            // given
            email = "asdf@gmail.com";

            // when
            VolunteerEmail volunteerEmail = new VolunteerEmail(email);

            // then
            assertThat(volunteerEmail.getEmail()).isEqualTo(email);
        }

        @Test
        @DisplayName("예외: 이메일이 null인 경우")
        void throwExceptionWhenEmailIsNull() {
            // given
            email = null;

            // when
            // then
            assertThatThrownBy(() -> new VolunteerEmail(email))
                .isInstanceOf(VolunteerBadRequestException.class);
        }

        @Test
        @DisplayName("예외: 이메일이 blank인 경우")
        void throwExceptionWhenEmailIsBlank() {
            // given
            email = " ";

            // when
            // then
            assertThatThrownBy(() -> new VolunteerEmail(email))
                .isInstanceOf(VolunteerBadRequestException.class);
        }

        @Test
        @DisplayName("예외: 이메일 형식에 맞지 않는 경우")
        void throwExceptionWhenEmailIsNotMatched() {
            // given
            email = "asdfnaver.com";

            // when
            // then
            assertThatThrownBy(() -> new VolunteerEmail(email))
                .isInstanceOf(VolunteerBadRequestException.class);
        }
    }
}
