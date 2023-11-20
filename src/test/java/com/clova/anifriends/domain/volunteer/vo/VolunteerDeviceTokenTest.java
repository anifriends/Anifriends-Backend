package com.clova.anifriends.domain.volunteer.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VolunteerDeviceTokenTest {

    @Nested
    @DisplayName("VolunteerDeviceToken 생성 시")
    class newVolunteerDeviceTokenTest {

        String deviceToken;

        @Test
        @DisplayName("성공")
        void success() {
            // given
            deviceToken = "token";

            // when
            VolunteerDeviceToken volunteerDeviceToken = new VolunteerDeviceToken(deviceToken);

            // then
            assertThat(volunteerDeviceToken.getDeviceToken()).isEqualTo(deviceToken);
        }

        @Test
        @DisplayName("예외(VolunteerBadRequestException): 토큰이 null인 경우")
        void throwExceptionWhenTokenIsNull() {
            // given
            deviceToken = null;

            // when
            Exception exception = catchException(() -> new VolunteerDeviceToken(deviceToken));

            // then
            assertThat(exception).isInstanceOf(VolunteerBadRequestException.class);
        }
    }
}
