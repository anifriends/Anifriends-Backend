package com.clova.anifriends.domain.volunteer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clova.anifriends.domain.auth.support.MockPasswordEncoder;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VolunteerTest {

    CustomPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new MockPasswordEncoder();
    }

    @Nested
    @DisplayName("Volunteer 생성 시")
    class newVolunteerTest {

        String email = "asdf@gmail.com";
        String password = "asdf1234";
        String birthDate;
        String phoneNumber = "010-1234-1234";
        String gender = "MALE";
        String name = "홍길동";


        @Test
        @DisplayName("성공")
        void success() {
            // given
            birthDate = "1990-01-01";

            // when
            Volunteer volunteer = new Volunteer(email, password, birthDate, phoneNumber, gender,
                name, passwordEncoder);

            // then
            assertThat(volunteer.getEmail()).isEqualTo(email);
        }

        @Test
        @DisplayName("예외: 생년월일 형식이 잘못된 경우")
        void throwExceptionWhenBirthDateFormatIsWrong() {
            // given
            birthDate = "1990:01:12";

            // when
            // then
            assertThatThrownBy(() -> new Volunteer(email, password, birthDate, phoneNumber, gender,
                name, passwordEncoder))
                .isInstanceOf(VolunteerBadRequestException.class);
        }
    }

}