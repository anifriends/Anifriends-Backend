package com.clova.anifriends.domain.review;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReviewImageTest {

    @Nested
    @DisplayName("ReviewImage 생성 시")
    class NewReviewImageTest {

        @Test
        @DisplayName("성공")
        void newReviewImage() {
            //given
            String imageUrl = "www.aws.s3.com/2";

            //when
            ReviewImage reviewImage = new ReviewImage(null, imageUrl);

            //then
            assertThat(reviewImage.getImageUrl()).isEqualTo(imageUrl);
        }
    }
}