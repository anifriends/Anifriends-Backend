package com.clova.anifriends.domain.shelter.wrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ShelterPasswordTest {

    @Nested
    @DisplayName("ShelterPassword 생성 시")
    class NewShelterPasswordTest {

        String password;

        @Test
        @DisplayName("성공")
        void newShelterPassword() {
            // given
            password = "asdfqwer123";

            // when
            ShelterPassword shelterPassword = new ShelterPassword(password);

            // then
            assertThat(shelterPassword.getPassword()).isEqualTo(password);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 비밀번호가 null인 경우")
        void throwExceptionWhenPasswordIsNull() {
            // given
            password = null;

            // when
            Exception exception = catchException(
                () -> new ShelterPassword(password));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 비밀번호가 blank인 경우")
        void throwExceptionWhenPasswordIsBlank() {
            // given
            password = null;

            // when
            Exception exception = catchException(
                () -> new ShelterPassword(password));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }
    }
}
