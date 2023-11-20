package com.clova.anifriends.domain.shelter.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ShelterDeviceTokenTest {

    @Nested
    @DisplayName("ShelterDeviceToken 생성 시")
    class newShelterDeviceTokenTest {

        String deviceToken;

        @Test
        @DisplayName("성공")
        void success() {
            // given
            deviceToken = "token";

            // when
            ShelterDeviceToken shelterDeviceToken = new ShelterDeviceToken(deviceToken);

            // then
            Assertions.assertThat(shelterDeviceToken.getDeviceToken()).isEqualTo(deviceToken);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 토큰이 null인 경우")
        void throwExceptionWhenTokenIsNull() {
            // given
            deviceToken = null;

            // when
            Exception exception = catchException(() -> new ShelterDeviceToken(deviceToken));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }
    }
}
