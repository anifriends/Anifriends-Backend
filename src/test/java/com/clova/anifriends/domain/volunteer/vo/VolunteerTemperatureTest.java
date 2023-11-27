package com.clova.anifriends.domain.volunteer.vo;

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

    @Nested
    @DisplayName("increase() 메서드 호출 시")
    class IncreaseTest {

        @Test
        @DisplayName("성공: 36 -> 39")
        void increase1() {
            // given
            int originTemperature = 36;
            int creaseTemperature = 3;
            VolunteerTemperature volunteerTemperature = new VolunteerTemperature(originTemperature);

            // when
            VolunteerTemperature updateVolunteerTemperature = volunteerTemperature.increase(
                creaseTemperature);

            // then
            assertThat(updateVolunteerTemperature.getTemperature()).isEqualTo(
                originTemperature + creaseTemperature);
        }

        @Test
        @DisplayName("성공: 99 -> 99")
        void increase2() {
            // given
            int maxTemperature = 99;
            int creaseTemperature = 3;
            VolunteerTemperature volunteerTemperature = new VolunteerTemperature(maxTemperature);

            // when
            VolunteerTemperature updateVolunteerTemperature = volunteerTemperature.increase(
                creaseTemperature);

            // then
            assertThat(updateVolunteerTemperature.getTemperature()).isEqualTo(
                maxTemperature);
        }
    }
}
