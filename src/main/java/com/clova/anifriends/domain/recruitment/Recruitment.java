package com.clova.anifriends.domain.recruitment;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.recruitment.wrapper.Content;
import com.clova.anifriends.domain.recruitment.wrapper.RecruitmentDeadline;
import com.clova.anifriends.domain.recruitment.wrapper.RecruitmentTime;
import com.clova.anifriends.domain.recruitment.wrapper.Title;
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
    private Title title;

    @Embedded
    private Content content;

    @Embedded
    private RecruitmentTime volunteerTime;

    @Embedded
    private RecruitmentDeadline recruitmentDeadline;

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
        this.title = new Title(title);
        this.content = new Content(content);
        this.volunteerTime = new RecruitmentTime(startTime, endTime);
        this.recruitmentDeadline = new RecruitmentDeadline(deadline, isClosed, capacity);
    }
}
