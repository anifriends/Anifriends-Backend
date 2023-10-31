package com.clova.anifriends.domain.volunteer.wrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VolunteerTemperatureTest {

    @Nested
    @DisplayName("VolunteerTemperature 생성 시")
    class newVolunteerTemperatureTest {

        int temperature;

        @Test
        @DisplayName("성공")
        void success() {
            // given
            temperature = 36;

            // when
            VolunteerTemperature volunteerTemperature = new VolunteerTemperature(temperature);

            // then
            assertThat(volunteerTemperature.getTemperature()).isEqualTo(temperature);
        }

        @Test
        @DisplayName("예외: 봉사자 체온이 99도 초과인 경우")
        void throwExceptionWhenVolunteerTemperatureIsOver99() {
            // given
            temperature = 101;

            // when
            // then
            assertThatThrownBy(() -> new VolunteerTemperature(temperature))
                .isInstanceOf(VolunteerBadRequestException.class);
        }
    }
}