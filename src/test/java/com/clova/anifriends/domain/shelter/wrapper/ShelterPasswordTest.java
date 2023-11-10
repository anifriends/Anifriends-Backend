package com.clova.anifriends.domain.shelter.wrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.auth.support.MockPasswordEncoder;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ShelterPasswordTest {

    CustomPasswordEncoder passwordEncoder = new MockPasswordEncoder();

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

    @Nested
    @DisplayName("checkPasswordEquals 메서드 실행 시")
    class CheckPasswordEqualsTest {

        @Test
        @DisplayName("성공")
        void checkPasswordEquals() {
            //given
            String password = "12345678";
            String encodedPassword = passwordEncoder.encodePassword(password);
            ShelterPassword shelterPassword = new ShelterPassword(encodedPassword);

            //when
            Exception exception
                = catchException(
                () -> shelterPassword.checkOldPasswordEquals(passwordEncoder, password));

            //then
            assertThat(exception).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 비밀번호가 같지 않음")
        void exceptionWhenNotEqualsPassword() {
            //given
            String password = "12345678";
            ShelterPassword shelterPassword = new ShelterPassword(password);
            String notEqualsPassword = password + "a";

            //when
            Exception exception = catchException(
                () -> shelterPassword.checkOldPasswordEquals(passwordEncoder, notEqualsPassword));

            //then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }
    }

}
