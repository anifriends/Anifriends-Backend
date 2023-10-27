package com.clova.anifriends.domain.recruitment;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.recruitment.wrapper.Capacity;
import com.clova.anifriends.domain.recruitment.wrapper.Content;
import com.clova.anifriends.domain.recruitment.wrapper.IsClosed;
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
    private Capacity capacity;

    @Embedded
    private Content content;

    @Embedded
    private IsClosed isClosed;

    @Column(name = "startTime")
    private LocalDateTime startTime;

    @Column(name = "endTime")
    private LocalDateTime endTime;

    @Column(name = "deadline")
    private LocalDateTime deadline;

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
        this.capacity = new Capacity(capacity);
        this.content = new Content(content);
        this.isClosed = new IsClosed(isClosed);
        this.startTime = startTime;
        this.endTime = endTime;
        this.deadline = deadline;
    }
}
