package com.clova.anifriends.domain.applicant;

import com.clova.anifriends.domain.applicant.wrapper.Status;
import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.volunteer.Volunteer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "applicant")
public class Applicant extends BaseTimeEntity {

    @Id
    @Column(name = "applicant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    protected Applicant() {
    }

    public Applicant(
        Recruitment recruitment,
        Volunteer volunteer,
        String status
    ) {
        this.recruitment = recruitment;
        this.volunteer = volunteer;
        this.status = Status.valueOf(status);
    }
}
