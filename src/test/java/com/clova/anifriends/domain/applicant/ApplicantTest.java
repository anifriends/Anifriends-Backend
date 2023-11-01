package com.clova.anifriends.domain.applicant;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ApplicantTest {

    @Nested
    @DisplayName("applicant 생성 시")
    class NewApplicantTest {

        @Test
        @DisplayName("성공")
        void newApplicant() {
            //given
            String status = ApplicantStatus.PENDING.getName();

            //when
            Applicant applicant = new Applicant(null, null, status);

            //then
            assertThat(applicant.getStatus()).isEqualTo(ApplicantStatus.valueOf(status));
        }
    }
}