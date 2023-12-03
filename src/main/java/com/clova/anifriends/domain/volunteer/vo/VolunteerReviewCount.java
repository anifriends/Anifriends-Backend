package com.clova.anifriends.domain.volunteer.vo;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.text.MessageFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VolunteerReviewCount {

    private static final int ZERO = 0;

    @Column(name = "review_count")
    private int reviewCount;

    public VolunteerReviewCount(int reviewCount) {
        validateReviewCount(reviewCount);
        this.reviewCount = reviewCount;
    }

    private void validateReviewCount(int reviewCount) {
        if (reviewCount < ZERO) {
            throw new VolunteerBadRequestException(ErrorCode.BAD_REQUEST,
                MessageFormat.format("봉사자의 봉사 후기 개수는 {0} 이상이어야 합니다.", ZERO));
        }
    }
}
