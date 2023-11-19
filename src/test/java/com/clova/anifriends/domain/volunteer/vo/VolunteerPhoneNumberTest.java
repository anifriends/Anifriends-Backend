package com.clova.anifriends.domain.volunteer.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VolunteerPhoneNumberTest {

    @Nested
    @DisplayName("VolunteerPhoneNumber 생성 시")
    class newVolunteerPhoneNumberTest {

        String phoneNumber;

        @Test
        @DisplayName("성공")
        void success() {
            // given
            phoneNumber = "010-1234-5678";

            // when
            VolunteerPhoneNumber volunteerPhoneNumber = new VolunteerPhoneNumber(phoneNumber);

            // then
            assertThat(volunteerPhoneNumber.getPhoneNumber()).isEqualTo(phoneNumber);
        }

        @Test
        @DisplayName("예외: 전화번호 형식이 잘못된 경우")
        void throwExceptionWhenPhoneNumberIsWrong() {
            // given
            phoneNumber = "01012345";

            // when
            // then
            assertThatThrownBy(() -> new VolunteerPhoneNumber(phoneNumber))
                .isInstanceOf(VolunteerBadRequestException.class);
        }
    }
}
