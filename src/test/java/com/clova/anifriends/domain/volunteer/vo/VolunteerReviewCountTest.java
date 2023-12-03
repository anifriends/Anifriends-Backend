package com.clova.anifriends.domain.volunteer.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class VolunteerReviewCountTest {

    @Nested
    @DisplayName("VolunteerReviewCount 생성 시")
    class NewVolunteerReviewCountTest {

        @ParameterizedTest
        @CsvSource({"0", "1", "10"})
        @DisplayName("성공")
        void newVolunteerReviewCount(String reviewCount) {
            //given
            int reviewCountNumber = Integer.parseInt(reviewCount);

            //when
            VolunteerReviewCount volunteerReviewCount = new VolunteerReviewCount(reviewCountNumber);

            //then
            assertThat(volunteerReviewCount.getReviewCount()).isEqualTo(reviewCountNumber);
        }

        @ParameterizedTest
        @CsvSource({"-1", "-5", "-10"})
        @DisplayName("예외(VolunteerBadRequestException): 봉사 후기 개수가 음수")
        void exceptionWhenInputInMinus(String reviewCount) {
            //given
            int reviewCountNumber = Integer.parseInt(reviewCount);

            //when
            Exception exception = catchException(() -> new VolunteerReviewCount(reviewCountNumber));

            //then
            assertThat(exception).isInstanceOf(VolunteerBadRequestException.class);
        }
    }

    @Nested
    @DisplayName("increase 메서드 호출 시")
    class IncreaseTest {

        @Test
        @DisplayName("성공: 리뷰 개수가 1 증가한다.")
        void increase() {
            //given
            VolunteerReviewCount volunteerReviewCount = new VolunteerReviewCount(0);

            //when
            VolunteerReviewCount updatedVolunteerReviewCount = volunteerReviewCount.increase();

            //then
            assertThat(updatedVolunteerReviewCount.getReviewCount()).isEqualTo(1);
            assertThat(updatedVolunteerReviewCount).isNotSameAs(volunteerReviewCount);
        }
    }

    @Nested
    @DisplayName("decrease 메서드 호출 시")
    class DecreaseTest {

        @Test
        @DisplayName("성공: 리뷰 개수가 1 감소한다.")
        void decrease() {
            //given
            VolunteerReviewCount volunteerReviewCount = new VolunteerReviewCount(1);

            //when
            VolunteerReviewCount decreasedVolunteerCount = volunteerReviewCount.decrease();

            //then
            assertThat(decreasedVolunteerCount.getReviewCount()).isEqualTo(0);
            assertThat(decreasedVolunteerCount).isNotSameAs(volunteerReviewCount);
        }

        @Test
        @DisplayName("예외(VolunteerBadRequestException): 리뷰 개수는 0 보다 작아질 수 없다.")
        void exceptionWhenTryDecreaseUnderZero() {
            //given
            VolunteerReviewCount volunteerReviewCount = new VolunteerReviewCount(0);

            //when
            Exception exception = catchException(volunteerReviewCount::decrease);

            //then
            assertThat(exception).isInstanceOf(VolunteerBadRequestException.class);
        }
    }
}
