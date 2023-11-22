package com.clova.anifriends.domain.recruitment.vo;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.text.MessageFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentApplicantCount {

    private static final int ZERO = 0;

    @Column(name = "applicant_count")
    private int applicantCount = 0;

    public RecruitmentApplicantCount(int applicantCount) {
        validateApplicantCount(applicantCount);
        this.applicantCount = applicantCount;
    }

    private void validateApplicantCount(int applicantCount) {
        if(applicantCount < ZERO) {
            throw new RecruitmentBadRequestException(
                MessageFormat.format("봉사 모집 신청자 수는 {0} 이상이어야 합니다.", ZERO));
        }
    }

    public RecruitmentApplicantCount increase() {
        return new RecruitmentApplicantCount(applicantCount + 1);
    }
}
