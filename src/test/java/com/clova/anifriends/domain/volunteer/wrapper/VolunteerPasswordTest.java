package com.clova.anifriends.domain.volunteer.wrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VolunteerPasswordTest {

    @Nested
    @DisplayName("VolunteerPassword 생성 시")
    class newVolunteerPasswordTest {

        String password;

        @Test
        @DisplayName("성공")
        void success() {
            // given
            password = "asdfqwer123";

            // when
            VolunteerPassword volunteerPassword = new VolunteerPassword(password);

            // then
            assertThat(volunteerPassword.getPassword()).isEqualTo(password);
        }

        @Test
        @DisplayName("예외: 비밀번호가 6자 미만인 경우")
        void throwExceptionWhenPasswordIsLessThanSix() {
            // given
            password = "asdf";

            // when
            // then
            assertThatThrownBy(() -> new VolunteerPassword(password))
                .isInstanceOf(VolunteerBadRequestException.class);
        }

        @Test
        @DisplayName("예외: 비밀번호가 16자 초과인 경우")
        void throwExceptionWhenPasswordIsOverThanSixteen() {
            // given
            password = "asdfqwer123asdfqwer123";

            // when
            // then
            assertThatThrownBy(() -> new VolunteerPassword(password))
                .isInstanceOf(VolunteerBadRequestException.class);
        }
    }
}