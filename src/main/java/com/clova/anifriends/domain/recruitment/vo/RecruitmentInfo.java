package com.clova.anifriends.domain.recruitment.vo;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentInfo {

    private static final int MIN_CAPACITY = 1;
    private static final int MAX_CAPACITY = 99;
    private static final int DELETABLE_HOURS = 36;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "is_closed")
    private boolean isClosed;

    @Column(name = "capacity")
    private int capacity;

    public RecruitmentInfo(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime deadline,
        boolean isClosed, int capacity) {
        validateNotNull(startTime, endTime, deadline);
        validateStartTimeIsAfterNow(startTime);
        validateEndTimeIsAfterStartTime(startTime, endTime);
        validateDeadlineIsBetweenNowAndStartTime(deadline, startTime);
        validateCapacitySize(capacity);
        this.startTime = startTime;
        this.endTime = endTime;
        this.deadline = deadline;
        this.isClosed = isClosed;
        this.capacity = capacity;
    }

    private void validateNotNull(LocalDateTime startTime, LocalDateTime endTime,
        LocalDateTime deadline) {
        if (Objects.isNull(startTime) || Objects.isNull(endTime) || Objects.isNull(deadline)) {
            throw new RecruitmentBadRequestException("봉사 시간은 필수입니다.");
        }
    }

    private void validateStartTimeIsAfterNow(LocalDateTime startTime) {
        LocalDateTime now = LocalDateTime.now();
        if (startTime.isBefore(now)) {
            throw new RecruitmentBadRequestException("봉사 시작 시간은 현재 시간 이후여야 합니다.");
        }
    }

    private void validateEndTimeIsAfterStartTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new RecruitmentBadRequestException("봉사 종료 시간은 봉사 시작 시간 이후여야 합니다.");
        }
    }

    private void validateDeadlineIsBetweenNowAndStartTime(LocalDateTime deadline,
        LocalDateTime startTime) {
        if (deadline.isBefore(LocalDateTime.now()) || deadline.isAfter(startTime)) {
            throw new RecruitmentBadRequestException("봉사 마감 시간은 현재 시간 이후, 봉시 시작 시간 이전이어야 합니다.");
        }
    }

    private void validateCapacitySize(int capacity) {
        if (capacity < MIN_CAPACITY || capacity > MAX_CAPACITY) {
            throw new RecruitmentBadRequestException(
                MessageFormat.format(
                    "봉사 모집 인원은 {0}명 이상, {1}명 이하여야 합니다.", MIN_CAPACITY, MAX_CAPACITY));
        }
    }

    public RecruitmentInfo closeRecruitment() {
        return new RecruitmentInfo(startTime, endTime, deadline, true, capacity);
    }

    public RecruitmentInfo updateRecruitmentInfo(
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime deadline,
        Integer capacity) {
        if(isNullArguments(startTime, endTime, deadline, capacity)) {
            return this;
        }
        return new RecruitmentInfo(
            updateStartTime(startTime),
            updateEndTime(endTime),
            updateDeadline(deadline),
            isClosed,
            updateCapacity(capacity));
    }

    private boolean isNullArguments(
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime deadline,
        Integer capacity) {
        return Objects.isNull(startTime)
            && Objects.isNull(endTime)
            && Objects.isNull(deadline)
            && Objects.isNull(capacity);
    }

    private LocalDateTime updateStartTime(LocalDateTime startTime) {
        return startTime != null ? startTime : this.startTime;
    }

    private LocalDateTime updateEndTime(LocalDateTime endTime) {
        return endTime != null ? endTime : this.endTime;
    }

    private LocalDateTime updateDeadline(LocalDateTime deadline) {
        return deadline != null ? deadline : this.deadline;
    }

    private int updateCapacity(Integer capacity) {
        return capacity != null ? capacity : this.capacity;
    }

    public void checkDeletable() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime maxDeletableTime = startTime.minusHours(DELETABLE_HOURS);
        if (maxDeletableTime.isBefore(now)) {
            throw new RecruitmentBadRequestException("봉사 시작 36시간 전까지만 삭제 가능합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecruitmentInfo that = (RecruitmentInfo) o;
        return isClosed == that.isClosed && capacity == that.capacity && Objects.equals(
            startTime, that.startTime) && Objects.equals(endTime, that.endTime)
            && Objects.equals(deadline, that.deadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, deadline, isClosed, capacity);
    }
}
