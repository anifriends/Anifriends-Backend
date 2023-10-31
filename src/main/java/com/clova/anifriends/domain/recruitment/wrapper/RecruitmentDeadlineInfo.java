package com.clova.anifriends.domain.recruitment.wrapper;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;

@Getter
@Embeddable
public class RecruitmentDeadlineInfo {

    private static final int MIN_CAPACITY = 1;
    private static final int MAX_CAPACITY = 99;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "is_closed")
    private boolean isClosed;

    @Column(name = "capacity")
    private int capacity;

    protected RecruitmentDeadlineInfo() {
    }

    public RecruitmentDeadlineInfo(LocalDateTime deadline, boolean isClosed, int capacity) {
        validateNotNull(deadline);
        validateDeadlineIsAfterNow(deadline);
        validateCapacitySize(capacity);
        this.deadline = deadline;
        this.isClosed = isClosed;
        this.capacity = capacity;
    }

    private void validateNotNull(LocalDateTime deadline) {
        if (Objects.isNull(deadline)) {
            throw new RecruitmentBadRequestException("마감 시간은 필수입니다.");
        }
    }

    private void validateDeadlineIsAfterNow(LocalDateTime deadline) {
        if (deadline.isBefore(LocalDateTime.now())) {
            throw new RecruitmentBadRequestException("봉사 마감 시간은 현재 시간 이후 여야 합니다.");
        }
    }

    private void validateCapacitySize(int capacity) {
        if (capacity < MIN_CAPACITY || capacity > MAX_CAPACITY) {
            throw new RecruitmentBadRequestException("봉사 모집 인원은 1명 이상, 99명 이하 여야 합니다.");
        }
    }
}
