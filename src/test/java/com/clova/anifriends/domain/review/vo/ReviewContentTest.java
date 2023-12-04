package com.clova.anifriends.domain.review.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.review.exception.ReviewBadRequestException;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReviewContentTest {

    @Nested
    @DisplayName("ReviewContent 생성 시")
    class newReviewContentTest {

        @Test
        @DisplayName("성공: 내용이 10자 이상")
        void newReviewContent1() {
            //given
            String content = "1234567890";

            //when
            Exception exception = catchException(() -> new ReviewContent(content));

            //then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공: 내용이 300자 이하")
        void newReviewContent2() {
            //given
            String content = String.join("", Collections.nCopies(300, "a"));

            //when
            Exception exception = catchException(() -> new ReviewContent(content));

            //then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("예외(ReviewBadRequestException): 내용이 10자 미만")
        void exceptionWhenContentUnder() {
            //given
            String content = "123456789";

            //when
            Exception exception = catchException(() -> new ReviewContent(content));

            //then
            assertThat(exception).isInstanceOf(ReviewBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ReviewBadRequestException): 내용이 300자 초과")
        void exceptionWhenContentOver() {
            //given
            String content = String.join("", Collections.nCopies(301, "a"));
            ;

            //when
            Exception exception = catchException(() -> new ReviewContent(content));

            //then
            assertThat(exception).isInstanceOf(ReviewBadRequestException.class);
        }
    }
}
