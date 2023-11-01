package com.clova.anifriends.domain.recruitment.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentInfo {

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

    public RecruitmentInfo(
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime deadline,
        boolean isClosed,
        int capacity
    ) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.deadline = deadline;
        this.isClosed = isClosed;
        this.capacity = capacity;
    }
}
