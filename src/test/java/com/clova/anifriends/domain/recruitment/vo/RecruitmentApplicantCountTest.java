package com.clova.anifriends.domain.recruitment.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RecruitmentApplicantCountTest {

    @Nested
    @DisplayName("RecruitmentApplicantCount 생성 시")
    class NewRecruitmentApplicantCount {

        @Test
        @DisplayName("성공")
        void newRecruitmentApplicantCount() {
            //given
            int applicantCount = 10;

            //when
            RecruitmentApplicantCount recruitmentApplicantCount = new RecruitmentApplicantCount(
                applicantCount);

            //then
            assertThat(recruitmentApplicantCount.getApplicantCount()).isEqualTo(applicantCount);
        }

        @ParameterizedTest
        @CsvSource({"-2", "-1"})
        @DisplayName("예외(RecruitmentBadRequestException): 0보다 작은 입력값")
        void exceptionWhenLT0(String minus) {
            //given
            int applicantCountLT0 = Integer.parseInt(minus);

            //when
            Exception exception = catchException(
                () -> new RecruitmentApplicantCount(applicantCountLT0));

            //then
            assertThat(exception).isInstanceOf(RecruitmentBadRequestException.class);
        }
    }

    @Nested
    @DisplayName("increase 메서드 호출 시")
    class IncreaseTest {

        @Test
        @DisplayName("성공")
        void increase() {
            //given
            RecruitmentApplicantCount applicantCount = new RecruitmentApplicantCount(10);

            //when
            RecruitmentApplicantCount updatedApplicantCount = applicantCount.increase();

            //then
            assertThat(updatedApplicantCount.getApplicantCount())
                .isEqualTo(applicantCount.getApplicantCount() + 1);
        }
    }
}