package com.clova.anifriends.domain.recruitment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RecruitmentImageTest {

    @Nested
    @DisplayName("RecruitmentImage 생성 시")
    class NewRecruitmentImageTest {

        @Test
        @DisplayName("성공")
        void newRecruitment() {
            //given
            String imageUrl = "www.aws.s3.com/2";

            //when
            RecruitmentImage recruitmentImage = new RecruitmentImage(null, imageUrl);

            //then
            assertThat(recruitmentImage.getImageUrl()).isEqualTo(imageUrl);
        }
    }
}