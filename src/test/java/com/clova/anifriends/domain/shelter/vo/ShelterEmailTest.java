package com.clova.anifriends.domain.shelter.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ShelterEmailTest {

    @Nested
    @DisplayName("ShelterEmail 생성 시")
    class NewShelterEmailTest {

        String email;

        @Test
        @DisplayName("성공")
        void newShelterEmail() {
            // given
            email = "test@naver.com";

            // when
            ShelterEmail shelterEmail = new ShelterEmail(email);

            // then
            assertThat(shelterEmail.getEmail()).isEqualTo(email);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 이메일이 null")
        void throwExceptionWhenEmailIsNull() {
            // given
            email = null;

            // when
            Exception exception = catchException(
                () -> new ShelterEmail(email));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 이메일이 빈 값")
        void throwExceptionWhenEmailIsBlank() {
            // given
            email = "";

            // when
            Exception exception = catchException(
                () -> new ShelterEmail(email));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }
    }
}
