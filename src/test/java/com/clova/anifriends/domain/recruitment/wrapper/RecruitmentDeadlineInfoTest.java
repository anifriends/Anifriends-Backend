package com.clova.anifriends.domain.recruitment.wrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RecruitmentDeadlineInfoTest {

    @Nested
    @DisplayName("RecruitmentDeadlineInfo 생성 시")
    class NewDeadlineTest {

        LocalDateTime deadline = LocalDateTime.now().plusHours(1);
        boolean isClosed = false;
        int capacity = 10;

        @Test
        @DisplayName("성공")
        void success() {
            //given
            //when
            RecruitmentDeadlineInfo deadlineInfo = new RecruitmentDeadlineInfo(deadline, isClosed,
                capacity);

            //then
            assertThat(deadlineInfo.getDeadline()).isEqualTo(deadline);
            assertThat(deadlineInfo.isClosed()).isEqualTo(isClosed);
            assertThat(deadlineInfo.getCapacity()).isEqualTo(capacity);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): deadline이 null")
        void exceptionWhenDeadlineIsNull() {
            //given
            LocalDateTime nullDeadline = null;

            //when
            //then
            assertThatThrownBy(
                () -> new RecruitmentDeadlineInfo(nullDeadline, isClosed, capacity)).isInstanceOf(
                RecruitmentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): deadline이 현재 시간 이전")
        void exceptionWhenDeadlineIsBeforeNow() {
            //given
            LocalDateTime deadlineBeforeNow = LocalDateTime.now().minusMinutes(1);

            //when
            //then
            assertThatThrownBy(() -> new RecruitmentDeadlineInfo(deadlineBeforeNow, isClosed,
                capacity)).isInstanceOf(RecruitmentBadRequestException.class);
        }

        @ParameterizedTest
        @CsvSource({
            "0", "100"
        })
        @DisplayName("예외(RecruitmentBadRequestException): capacity가 범위를 넘음")
        void exceptionWhenCapacityOutOfSize(String size) {
            //given
            int capacityOutOfSize = Integer.parseInt(size);

            //when
            //then
            assertThatThrownBy(() -> new RecruitmentDeadlineInfo(deadline, isClosed,
                capacityOutOfSize)).isInstanceOf(RecruitmentBadRequestException.class);
        }
    }
}