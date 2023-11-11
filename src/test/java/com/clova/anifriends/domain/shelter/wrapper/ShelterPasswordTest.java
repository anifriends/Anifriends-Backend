package com.clova.anifriends.domain.shelter.wrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.auth.support.MockPasswordEncoder;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import org.junit.jupiter.api.BeforeEach;
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
    @DisplayName("updatePassword 메서드 실행 시")
    class UpdatePasswordTest {

        String rawOldPassword;
        String rawNewPassword;

        @BeforeEach
        void setUp() {
            rawOldPassword = "asdf123!";
            rawNewPassword = rawOldPassword + "a";
        }

        @Test
        @DisplayName("성공: 입력된 현재 비밀번호와 보호소 비밀번호가 같음")
        void updatePassword() {
            //given
            String encodedPassword = passwordEncoder.encodePassword(rawOldPassword);
            ShelterPassword shelterPassword = new ShelterPassword(encodedPassword);

            //when
            Exception exception = catchException(
                () -> shelterPassword.updatePassword(passwordEncoder, rawOldPassword,
                    rawNewPassword));

            //then
            assertThat(exception).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 입력된 현재 비밀번호와 보호소 비밀번호가 같지 않음")
        void exceptionWhenNotEqualsPassword() {
            //given
            ShelterPassword shelterPassword = new ShelterPassword(rawOldPassword);
            String notEqualsPassword = rawOldPassword + "a";

            //when
            Exception exception = catchException(
                () -> shelterPassword.updatePassword(passwordEncoder, notEqualsPassword,
                    rawNewPassword));

            //then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 입력된 새로운 비밀번호와 보호소 비밀번호가 같음")
        void exceptionWhenEqualsNewPassword() {
            //given
            ShelterPassword shelterPassword = new ShelterPassword(rawOldPassword);
            String notEqualsPassword = rawOldPassword + "a";

            //when
            Exception exception = catchException(
                () -> shelterPassword.updatePassword(passwordEncoder, notEqualsPassword,
                    rawNewPassword));

            //then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }
    }

}
