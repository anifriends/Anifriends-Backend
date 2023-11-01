package com.clova.anifriends.domain.volunteer;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "voulunteer_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VolunteerImage extends BaseTimeEntity {

    @Id
    @Column(name = "volunteer_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long volunteerImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    @Column(name = "image_url")
    private String imageUrl;

    public VolunteerImage(Volunteer volunteer, String imageUrl) {
        this.volunteer = volunteer;
        this.imageUrl = imageUrl;
    }
}

