package com.clova.anifriends.domain.recruitment.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RecruitmentContentTest {

    @Nested
    @DisplayName("RecruitmentContent 생성 시")
    class newContentTest {

        @Test
        @DisplayName("성공")
        void newContent() {
            //given
            String content = "본문";

            //when
            RecruitmentContent recruitmentContent = new RecruitmentContent(content);

            //then
            assertThat(recruitmentContent.getContent()).isEqualTo(content);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): 본문이 null")
        void exceptionWhenContentIsNull() {
            //given
            String content = null;

            //when
            Exception exception = catchException(() -> new RecruitmentContent(content));

            //then
            assertThat(exception).isInstanceOf(RecruitmentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): 제목이 1자 미만")
        void exceptionWhenContentLengthZero() {
            //given
            String content = "";

            //when
            Exception exception = catchException(() -> new RecruitmentContent(content));

            //then
            assertThat(exception).isInstanceOf(RecruitmentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): 제목이 1000자 초과")
        void exceptionWhenContentOver1000() {
            //given
            String content = "a".repeat(1001);

            //when
            Exception exception = catchException(() -> new RecruitmentContent(content));

            //then
            assertThat(exception).isInstanceOf(RecruitmentBadRequestException.class);
        }
    }

    @Nested
    @DisplayName("updateContent 메서드 호출 시")
    class UpdateContentTest {

        RecruitmentContent recruitmentContent;

        @BeforeEach
        void setUp() {
            String content = "content";
            recruitmentContent = new RecruitmentContent(content);
        }

        @Test
        @DisplayName("성공: 입력값이 null 이 아닐 때, 값이 업데이트 된 RecruitmentContent를 반환")
        void updateContent() {
            //given
            String newContent = recruitmentContent.getContent() + "a";

            //when
            RecruitmentContent updatedContent = recruitmentContent.updateContent(newContent);

            //then
            assertThat(updatedContent.getContent()).isEqualTo(newContent);
            assertThat(updatedContent).isNotEqualTo(recruitmentContent);
        }

        @Test
        @DisplayName("성공: 입력값이 null 일 때, 값이 업데이트 되지 않은 자기 자신을 반환")
        void updateContentWhenContentIsNull() {
            //given
            String nullContent = null;

            //when
            RecruitmentContent updatedContent = recruitmentContent.updateContent(nullContent);

            //then
            assertThat(updatedContent.getContent()).isEqualTo(recruitmentContent.getContent());
            assertThat(updatedContent).isEqualTo(recruitmentContent);
        }
    }
}
