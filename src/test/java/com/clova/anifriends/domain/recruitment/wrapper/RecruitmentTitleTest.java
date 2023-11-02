package com.clova.anifriends.domain.recruitment.wrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RecruitmentTitleTest {

    @Nested
    @DisplayName("RecruitmentTitle 생성 시")
    class newTitleTest {

        @Test
        @DisplayName("성공")
        void newRecruitmentTitle() {
            //given
            String title = "타이틀";

            //when
            RecruitmentTitle recruitmentTitle = new RecruitmentTitle(title);

            //then
            assertThat(recruitmentTitle.getTitle()).isEqualTo(title);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): 제목이 null")
        void exceptionWhenTitleIsNull() {
            //given
            String title = null;

            //when
            Exception exception = catchException(() -> new RecruitmentTitle(title));

            //then
            assertThat(exception).isInstanceOf(RecruitmentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): 제목이 1자 미만")
        void exceptionWhenTitleLengthZero() {
            //given
            String title = "";

            //when
            Exception exception = catchException(() -> new RecruitmentTitle(title));

            //then
            assertThat(exception).isInstanceOf(RecruitmentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): 제목이 100자 초과")
        void exceptionWhenTitleOver100() {
            //given
            String title = "a".repeat(101);

            //when
            Exception exception = catchException(() -> new RecruitmentTitle(title));

            //then
            assertThat(exception).isInstanceOf(RecruitmentBadRequestException.class);
        }
    }
}
