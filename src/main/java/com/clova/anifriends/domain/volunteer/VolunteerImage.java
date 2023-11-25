package com.clova.anifriends.domain.volunteer;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    @Column(name = "image_url")
    private String imageUrl;

    public VolunteerImage(Volunteer volunteer, String imageUrl) {
        validateVolunteer(volunteer);
        this.volunteer = volunteer;
        validateImageUrl(imageUrl);
        this.imageUrl = imageUrl;
    }

    private void validateVolunteer(Volunteer value) {
        if (value == null) {
            throw new VolunteerBadRequestException(ErrorCode.BAD_REQUEST, "봉사자는 필수 항목입니다.");
        }
    }

    private void validateImageUrl(String value) {
        if (value == null || value.isBlank()) {
            throw new VolunteerBadRequestException(ErrorCode.BAD_REQUEST, "이미지 url은 필수 항목입니다.");
        }
    }

    public boolean isSameWith(String imageUrl) {
        return this.imageUrl.equals(imageUrl);
    }

    public boolean isDifferentFrom(String imageUrl) {
        return !this.imageUrl.equals(imageUrl);
    }
}
