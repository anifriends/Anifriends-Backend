package com.clova.anifriends.domain.recruitment.wrapper;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;

@Getter
@Embeddable
public class RecruitmentTime {

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    protected RecruitmentTime() {
    }

    public RecruitmentTime(LocalDateTime startTime, LocalDateTime endTime) {
        validateNotNull(startTime, endTime);
        validateStartTimeIsAfterNow(startTime);
        validateEndTimeIsAfterStartTime(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void validateNotNull(LocalDateTime startTime, LocalDateTime endTime) {
        if(Objects.isNull(startTime) || Objects.isNull(endTime)) {
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
}
