package com.clova.anifriends.domain.recruitment;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.recruitment.wrapper.RecruitmentContent;
import com.clova.anifriends.domain.recruitment.wrapper.RecruitmentInfo;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "recruitment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment extends BaseTimeEntity {

    public static final boolean IS_CLOSED_DEFAULT = false;

    @Id
    @Column(name = "recruitment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recruitmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @OneToMany(mappedBy = "recruitment", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RecruitmentImage> imageUrls = new ArrayList<>();

    @OneToMany(mappedBy = "recruitment", fetch = FetchType.LAZY)
    private List<Applicant> applications = new ArrayList<>();

    @Embedded
    private RecruitmentTitle title;

    @Embedded
    private RecruitmentContent content;

    @Embedded
    private RecruitmentInfo info;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Recruitment(
        Shelter shelter,
        String title,
        int capacity,
        String content,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime deadline,
        List<String> imageUrls
    ) {
        this.shelter = shelter;
        this.title = new RecruitmentTitle(title);
        this.content = new RecruitmentContent(content);
        this.info = new RecruitmentInfo(startTime, endTime, deadline, IS_CLOSED_DEFAULT, capacity);
        this.imageUrls = imageUrls.stream()
            .map(url -> new RecruitmentImage(this, url))
            .toList();
    }

    public Long getRecruitmentId() {
        return recruitmentId;
    }

    public String getTitle() {
        return title.getTitle();
    }

    public int getCapacity() {
        return info.getCapacity();
    }

    public String getContent() {
        return content.getContent();
    }

    public LocalDateTime getStartTime() {
        return info.getStartTime();
    }

    public LocalDateTime getEndTime() {
        return info.getEndTime();
    }

    public Boolean isClosed() {
        return info.isClosed();
    }

    public LocalDateTime getDeadline() {
        return info.getDeadline();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<String> getImageUrls() {
        return imageUrls.stream()
            .map(RecruitmentImage::getImageUrl)
            .toList();
    }

    public int getApplicantCount() {
        return applications.size();
    }

    public Shelter getShelter() {
        return shelter;
    }
}
