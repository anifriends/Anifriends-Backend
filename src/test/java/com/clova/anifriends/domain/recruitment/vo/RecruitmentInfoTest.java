package com.clova.anifriends.domain.recruitment.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RecruitmentInfoTest {

    @Nested
    @DisplayName("RecruitmentTime 생성 시")
    class newTimeTest {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.plusHours(1);
        LocalDateTime endTime = startTime.plusMinutes(1);
        LocalDateTime deadline = startTime.minusMinutes(1);
        boolean isClosed = false;
        int capacity = 10;


        @Test
        @DisplayName("성공")
        void newRecruitmentInfo() {
            //given
            //when
            RecruitmentInfo recruitmentTime = new RecruitmentInfo(startTime, endTime, deadline,
                isClosed, capacity);

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
            Exception exception = catchException(
                () -> new RecruitmentInfo(nullStartTime, endTime, deadline, isClosed, capacity));

            //then
            assertThat(exception).isInstanceOf(RecruitmentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): endTime이 null")
        void exceptionWhenEndTimeIsNull() {
            //given
            LocalDateTime nullEndTime = null;

            //when
            Exception exception = catchException(
                () -> new RecruitmentInfo(startTime, nullEndTime, deadline, isClosed, capacity));

            //then
            assertThat(exception).isInstanceOf(RecruitmentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): deadline이 null")
        void exceptionWhenDeadlineIsNull() {
            //given
            LocalDateTime nullDeadline = null;

            //when
            Exception exception = catchException(
                () -> new RecruitmentInfo(startTime, endTime, nullDeadline, isClosed, capacity));

            //then
            assertThat(exception).isInstanceOf(RecruitmentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): startTime이 현재 시간보다 이전")
        void exceptionWhenStartTimeIsBeforeNow() {
            //given
            LocalDateTime beforeStartTime = now.minusMinutes(1);

            //when
            Exception exception = catchException(
                () -> new RecruitmentInfo(beforeStartTime, endTime, deadline, isClosed, capacity));

            //then
            assertThat(exception).isInstanceOf(RecruitmentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): endTime이 시작 시간보다 이전")
        void exceptionWhenEndTimeIsBeforeStartTime() {
            //given
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startTime = now.plusHours(1);
            LocalDateTime beforeEndTime = startTime.minusSeconds(1);

            //when
            Exception exception = catchException(
                () -> new RecruitmentInfo(startTime, beforeEndTime, deadline, isClosed, capacity));

            //then
            assertThat(exception).isInstanceOf(RecruitmentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): deadline이 현재 시간 이전")
        void exceptionWhenDeadlineIsBeforeNow() {
            //given
            LocalDateTime deadlineBeforeNow = LocalDateTime.now().minusMinutes(1);

            //when
            Exception exception = catchException(
                () -> new RecruitmentInfo(startTime, endTime, deadlineBeforeNow, isClosed,
                    capacity));

            //then
            assertThat(exception)
                .isInstanceOf(RecruitmentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): deadline이 봉사 시작 시간 이후")
        void exceptionWhenDeadlineIsAfterStartTime() {
            //given
            LocalDateTime deadlineAfterStartTime = startTime.plusMinutes(1);

            //when
            Exception exception = catchException(() -> new RecruitmentInfo(
                startTime, endTime, deadlineAfterStartTime, isClosed, capacity));

            //then
            assertThat(exception)
                .isInstanceOf(RecruitmentBadRequestException.class);
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
            Exception exception = catchException(
                () -> new RecruitmentInfo(startTime, endTime, deadline, isClosed,
                    capacityOutOfSize));

            //then
            assertThat(exception)
                .isInstanceOf(RecruitmentBadRequestException.class);
        }
    }

    @Nested
    @DisplayName("closeRecruitment 메서드 호출 시")
    class CloseRecruitmentTest {

        @Test
        @DisplayName("성공")
        void closeRecruitment() {
            //given
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startTime = now.plusHours(1);
            LocalDateTime endTime = startTime.plusMinutes(1);
            LocalDateTime deadline = startTime.minusMinutes(1);
            boolean isClosed = false;
            int capacity = 10;
            RecruitmentInfo recruitmentInfo
                = new RecruitmentInfo(startTime, endTime, deadline, isClosed, capacity);

            //when
            RecruitmentInfo updatedRecruitmentInfo = recruitmentInfo.closeRecruitment();

            //then
            assertThat(updatedRecruitmentInfo.isClosed()).isTrue();
        }
    }

    @Nested
    @DisplayName("updateRecruitmentInfo 메서드 호출 시")
    class UpdateRecruitmentInfoTest {

        RecruitmentInfo recruitmentInfo;

        @BeforeEach
        void setUp() {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startTime = now.plusMonths(1);
            LocalDateTime endTime = startTime.plusHours(1);
            LocalDateTime deadline = startTime.minusDays(1);
            int capacity = 10;
            recruitmentInfo = new RecruitmentInfo(startTime, endTime, deadline, false, capacity);
        }

        @Test
        @DisplayName("성공: 입력값이 null이 아닐 때, 값이 업데이트 된 RecruitmentInfoTest를 반환")
        void updateRecruitmentInfo() {
            //given
            LocalDateTime newStartTime = recruitmentInfo.getStartTime().plusDays(1);
            LocalDateTime newEndTime = recruitmentInfo.getEndTime().plusDays(1);
            LocalDateTime newDeadline = recruitmentInfo.getDeadline().plusDays(1);
            int newCapacity = recruitmentInfo.getCapacity() + 1;

            //when
            RecruitmentInfo updatedRecruitmentInfo = recruitmentInfo.updateRecruitmentInfo(
                newStartTime, newEndTime, newDeadline, newCapacity);

            //then
            assertThat(updatedRecruitmentInfo.getStartTime()).isEqualTo(newStartTime);
            assertThat(updatedRecruitmentInfo.getEndTime()).isEqualTo(newEndTime);
            assertThat(updatedRecruitmentInfo.getDeadline()).isEqualTo(newDeadline);
            assertThat(updatedRecruitmentInfo.getDeadline()).isEqualTo(newDeadline);
            assertThat(updatedRecruitmentInfo).isNotEqualTo(recruitmentInfo);
        }

        @Test
        @DisplayName("성공: 입력값이 null일 때, 값이 업데이트 되지 않은 자기 자신을 반환")
        void updateRecruitmentInfoWhenInputIsNull() {
            //given
            LocalDateTime nullStartTime = null;
            LocalDateTime nullEndTime = null;
            LocalDateTime nullDeadline = null;
            Integer nullCapacity = null;

            //when
            RecruitmentInfo updatedRecruitmentInfo = recruitmentInfo.updateRecruitmentInfo(
                nullStartTime, nullEndTime, nullDeadline, nullCapacity);

            //then
            assertThat(updatedRecruitmentInfo.getStartTime())
                .isEqualTo(recruitmentInfo.getStartTime());
            assertThat(updatedRecruitmentInfo.getEndTime())
                .isEqualTo(recruitmentInfo.getEndTime());
            assertThat(updatedRecruitmentInfo.getDeadline())
                .isEqualTo(recruitmentInfo.getDeadline());
            assertThat(updatedRecruitmentInfo.getCapacity())
                .isEqualTo(recruitmentInfo.getCapacity());
            assertThat(updatedRecruitmentInfo).isEqualTo(recruitmentInfo);
        }

        @Test
        @DisplayName("성공: null이 입력되지 않은 값에 대해서만 업테이트")
        void updateRecruitmentInfoWhenUpdateStartTimeAndEndTime() {
            //given
            LocalDateTime newStartTime = recruitmentInfo.getStartTime().plusDays(1);
            LocalDateTime newEndTime = newStartTime.plusHours(1);
            LocalDateTime nullDeadline = null;
            Integer nullCapacity = null;

            //when
            RecruitmentInfo updatedRecruitmentInfo = recruitmentInfo.updateRecruitmentInfo(
                newStartTime, newEndTime, nullDeadline, nullCapacity);

            //then
            assertThat(updatedRecruitmentInfo.getStartTime())
                .isEqualTo(newStartTime);
            assertThat(updatedRecruitmentInfo.getEndTime())
                .isEqualTo(newEndTime);
            assertThat(updatedRecruitmentInfo.getDeadline())
                .isEqualTo(recruitmentInfo.getDeadline());
            assertThat(updatedRecruitmentInfo.getCapacity())
                .isEqualTo(recruitmentInfo.getCapacity());
        }
    }

    @Nested
    @DisplayName("checkDeletable 메서드 호출 시")
    class CheckDeletableTest {

        boolean isClosed;
        int capacity;

        @BeforeEach
        void setUp() {
            isClosed = false;
            capacity = 10;
        }

        @Test
        @DisplayName("성공: 봉사 시작 시간 36시간 이전")
        void checkDeletable() {
            //given
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startTime = now.plusMonths(1);
            LocalDateTime endTime = startTime.plusHours(1);
            LocalDateTime deadline = startTime.minusDays(1);
            RecruitmentInfo recruitmentInfo = new RecruitmentInfo(startTime, endTime, deadline,
                isClosed, capacity);

            //when
            Exception exception = catchException(() -> recruitmentInfo.checkDeletable());

            //then
            assertThat(exception).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): 봉사 시작 시간 36시간 이내")
        void exceptionWhenStartTimeNear36Hours() {
            //given
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startTime = now.plusHours(35);
            LocalDateTime endTime = startTime.plusHours(1);
            LocalDateTime deadline = startTime.minusHours(1);
            RecruitmentInfo recruitmentInfo = new RecruitmentInfo(startTime, endTime, deadline,
                isClosed, capacity);

            //when
            Exception exception = catchException(() -> recruitmentInfo.checkDeletable());

            //then
            assertThat(exception).isInstanceOf(RecruitmentBadRequestException.class);
        }
    }
}
