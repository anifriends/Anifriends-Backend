package com.clova.anifriends.domain.volunteer.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clova.anifriends.domain.volunteer.exception.NotFoundVolunteerGenderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VolunteerGenderTest {

    @Nested
    @DisplayName("VolunteerGender ")
    class matchVolunteerGenderTest {

        String gender;

        @Test
        @DisplayName("성공")
        void success() {
            // given
            gender = "MALE";

            // when
            VolunteerGender volunteerGender = VolunteerGender.from(gender);

            // then
            assertThat(volunteerGender.getName()).isEqualTo(gender);
        }

        @Test
        @DisplayName("예외: VolunteerGender가 null인 경우")
        void throwExceptionWhenVolunteerGenderIsNull() {
            // given
            gender = null;

            // when
            // then
            assertThatThrownBy(() -> VolunteerGender.from(gender))
                .isInstanceOf(NotFoundVolunteerGenderException.class);
        }

        @Test
        @DisplayName("예외: VolunteerGender가 다른 문자열인 경우")
        void throwExceptionWhenVolunteerGenderIsWrong() {
            // given
            gender = "ABCD";

            // when
            // then
            assertThatThrownBy(() -> VolunteerGender.from(gender))
                .isInstanceOf(NotFoundVolunteerGenderException.class);
        }
    }
}