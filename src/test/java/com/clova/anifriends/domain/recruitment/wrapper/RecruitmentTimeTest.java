package com.clova.anifriends.domain.recruitment.wrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RecruitmentTimeTest {

    @Nested
    @DisplayName("RecruitmentTime 생성 시")
    class newTimeTest {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.plusMinutes(1);
        LocalDateTime endTime = startTime.plusMinutes(1);

        @Test
        @DisplayName("성공")
        void success() {
            //given
            //when
            RecruitmentTime recruitmentTime = new RecruitmentTime(startTime, endTime);

            //then
            assertThat(recruitmentTime.getStartTime()).isEqualTo(startTime);
            assertThat(recruitmentTime.getEndTime()).isEqualTo(endTime);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): startTime이 null")
        void exceptionWhenStartTimeIsNull() {
            //given
            LocalDateTime nullStartTime = null;

            //when
            assertThatThrownBy(() -> new RecruitmentTime(nullStartTime, endTime))
                .isInstanceOf(RecruitmentBadRequestException.class);
        }
        @Test
        @DisplayName("예외(RecruitmentBadRequestException): endTime이 null")
        void exceptionWhenEndTimeIsNull() {
            //given
            LocalDateTime nullEndTime = null;

            //when
            assertThatThrownBy(() -> new RecruitmentTime(startTime, nullEndTime))
                .isInstanceOf(RecruitmentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): startTime이 현재 시간보다 이전")
        void exceptionWhenStartTimeIsBeforeNow() {
            //given
            LocalDateTime beforeStartTime = now.minusMinutes(1);

            //when
            //then
            assertThatThrownBy(() -> new RecruitmentTime(beforeStartTime, endTime))
                .isInstanceOf(RecruitmentBadRequestException.class);
        }
        @Test
        @DisplayName("예외(RecruitmentBadRequestException): endTime이 시작 시간보다 이전")
        void exceptionWhenEndTimeIsBeforeStartTime() {
            //given
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startTime = now.plusHours(1);
            LocalDateTime beforeEndTime = startTime.minusSeconds(1);

            //when
            //then
            assertThatThrownBy(() -> new RecruitmentTime(startTime, beforeEndTime))
                .isInstanceOf(RecruitmentBadRequestException.class);
        }

    }
}