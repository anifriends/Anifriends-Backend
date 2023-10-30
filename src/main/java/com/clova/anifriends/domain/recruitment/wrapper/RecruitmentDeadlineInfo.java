package com.clova.anifriends.domain.recruitment.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class RecruitmentDeadlineInfo {

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "is_closed")
    private boolean isClosed;

    @Column(name = "capacity")
    private int capacity;

    protected RecruitmentDeadlineInfo() {
    }

    public RecruitmentDeadlineInfo(LocalDateTime deadline, boolean isClosed, int capacity) {
        this.deadline = deadline;
        this.isClosed = isClosed;
        this.capacity = capacity;
    }
}
