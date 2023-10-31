package com.clova.anifriends.domain.recruitment.wrapper;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        void success() {
            //given
            String title = "타이틀";

            //when
            //then
            assertThatCode(() -> new RecruitmentTitle(title)).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): 제목이 null")
        void exceptionWhenTitleIsNull() {
            //given
            String title = null;

            //when
            //then
            assertThatThrownBy(() -> new RecruitmentTitle(title))
                .isInstanceOf(RecruitmentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): 제목이 1자 미만")
        void exceptionWhenTitleLengthZero() {
            //given
            String title = "";

            //when
            //then
            assertThatThrownBy(() -> new RecruitmentTitle(title))
                .isInstanceOf(RecruitmentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): 제목이 100자 초과")
        void exceptionWhenTitleOver100() {
            //given
            String title = "a".repeat(101);

            //when
            //then
            assertThatThrownBy(() -> new RecruitmentTitle(title))
                .isInstanceOf(RecruitmentBadRequestException.class);
        }
    }
}