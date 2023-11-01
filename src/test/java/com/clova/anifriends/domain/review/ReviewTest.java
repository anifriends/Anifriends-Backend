package com.clova.anifriends.domain.review;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReviewTest {

    @Nested
    @DisplayName("Review 생성 시")
    class NewReviewTest {

        @Test
        @DisplayName("성공")
        void newReview () {
            //given
            String content = "content";

            //when
            Review review = new Review(null, null, content);

            //then
            assertThat(review.getContent()).isEqualTo(content);
        }
    }
}