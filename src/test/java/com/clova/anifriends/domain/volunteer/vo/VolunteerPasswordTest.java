package com.clova.anifriends.domain.volunteer.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.auth.support.MockPasswordEncoder;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VolunteerPasswordTest {

    @Nested
    @DisplayName("VolunteerPassword 생성 시")
    class newVolunteerPasswordTest {

        CustomPasswordEncoder passwordEncoder = new MockPasswordEncoder();
        String password;

        @Test
        @DisplayName("성공")
        void success() {
            // given
            password = "asdfqwer123";

            // when
            VolunteerPassword volunteerPassword = new VolunteerPassword(password, passwordEncoder);

            // then
            assertThat(passwordEncoder.matchesPassword(password,
                volunteerPassword.getPassword())).isTrue();
        }

        @Test
        @DisplayName("예외(VolunteerBadRequestException): 비밀번호가 6자 미만인 경우")
        void throwExceptionWhenPasswordIsLessThanSix() {
            // given
            password = "asdf";

            // when
            // then
            assertThatThrownBy(() -> new VolunteerPassword(password, passwordEncoder))
                .isInstanceOf(VolunteerBadRequestException.class);
        }

        @Test
        @DisplayName("예외(VolunteerBadRequestException): 비밀번호가 16자 초과인 경우")
        void throwExceptionWhenPasswordIsOverThanSixteen() {
            // given
            password = "asdfqwer123asdfqwer123";

            // when
            // then
            assertThatThrownBy(() -> new VolunteerPassword(password, passwordEncoder))
                .isInstanceOf(VolunteerBadRequestException.class);
        }

        @Test
        @DisplayName("예외(VolunteerBadRequestException): 비밀번호가 null인 경우")
        void throwExceptionWhenPasswordIsNull() {
            // given
            password = null;

            // when
            Exception exception = catchException(
                () -> new VolunteerPassword(password, passwordEncoder));

            // then
            assertThat(exception).isInstanceOf(VolunteerBadRequestException.class);
        }
    }
}
