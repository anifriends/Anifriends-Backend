package com.clova.anifriends.domain.volunteer.wrapper;

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
            phoneNumber = "01012345678";

            // when
            VolunteerPhoneNumber volunteerPhoneNumber = new VolunteerPhoneNumber(phoneNumber);

            // then
            assertThat(volunteerPhoneNumber.getPhoneNumber()).isEqualTo(phoneNumber);
        }

        @Test
        @DisplayName("예외: 전화번호가 9자 미만인 경우")
        void throwExceptionWhenPhoneNumberIsLessThan9() {
            // given
            phoneNumber = "01012345";

            // when
            // then
            assertThatThrownBy(() -> new VolunteerPhoneNumber(phoneNumber))
                .isInstanceOf(VolunteerBadRequestException.class);
        }

        @Test
        @DisplayName("예외: 전화번호가 12자 초과인 경우")
        void throwExceptionWhenPhoneNumberIsMoreThan12() {
            // given
            phoneNumber = "010123456789";

            // when
            // then
            assertThatThrownBy(() -> new VolunteerPhoneNumber(phoneNumber))
                .isInstanceOf(VolunteerBadRequestException.class);
        }
    }
}
