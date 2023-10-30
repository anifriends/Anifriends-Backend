package com.clova.anifriends.domain.recruitment;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.recruitment.wrapper.RecruitmentContent;
import com.clova.anifriends.domain.recruitment.wrapper.RecruitmentDeadlineInfo;
import com.clova.anifriends.domain.recruitment.wrapper.RecruitmentTime;
import com.clova.anifriends.domain.recruitment.wrapper.RecruitmentTitle;
import com.clova.anifriends.domain.shelter.Shelter;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "recruitment")
public class Recruitment extends BaseTimeEntity {

    @Id
    @Column(name = "recruitment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recruitmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @Embedded
    private RecruitmentTitle title;

    @Embedded
    private RecruitmentContent content;

    @Embedded
    private RecruitmentTime time;

    @Embedded
    private RecruitmentDeadlineInfo deadlineInfo;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Recruitment() {
    }

    public Recruitment(
        Shelter shelter,
        String title,
        int capacity,
        String content,
        boolean isClosed,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime deadline
    ) {
        this.shelter = shelter;
        this.title = new RecruitmentTitle(title);
        this.content = new RecruitmentContent(content);
        this.time = new RecruitmentTime(startTime, endTime);
        this.deadlineInfo = new RecruitmentDeadlineInfo(deadline, isClosed, capacity);
    }
}
