package com.clova.anifriends.domain.recruitment.wrapper;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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

    private void validateNotNull(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime deadline) {
        if(Objects.isNull(startTime) || Objects.isNull(endTime) || Objects.isNull(deadline)) {
            throw new RecruitmentBadRequestException("봉사 시간은 필수입니다.");
        }
    }

    private void validateStartTimeIsAfterNow(LocalDateTime startTime) {
        LocalDateTime now = LocalDateTime.now();
        if(startTime.isBefore(now)) {
            throw new RecruitmentBadRequestException("봉사 시작 시간은 현재 시간 이후여야 합니다.");
        }
    }

    private void validateEndTimeIsAfterStartTime(LocalDateTime startTime, LocalDateTime endTime) {
        if(endTime.isBefore(startTime)) {
            throw new RecruitmentBadRequestException("봉사 종료 시간은 봉사 시작 시간 이후여야 합니다.");
        }
    }

    private void validateDeadlineIsBetweenNowAndStartTime(LocalDateTime deadline, LocalDateTime startTime) {
        if (deadline.isBefore(LocalDateTime.now()) || deadline.isAfter(startTime)) {
            throw new RecruitmentBadRequestException("봉사 마감 시간은 현재 시간 이후, 봉시 시작 시간 이전이어야 합니다.");
        }
    }

    private void validateCapacitySize(int capacity) {
        if (capacity < MIN_CAPACITY || capacity > MAX_CAPACITY) {
            throw new RecruitmentBadRequestException("봉사 모집 인원은 1명 이상, 99명 이하 여야 합니다.");
        }
    }
}
