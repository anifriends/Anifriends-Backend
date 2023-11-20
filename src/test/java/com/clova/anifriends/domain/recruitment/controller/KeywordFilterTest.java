package com.clova.anifriends.domain.recruitment.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class KeywordFilterTest {

    @Nested
    @DisplayName("getKeywordCondition 메서드 호출 시")
    class GetKeywordConditionTest {

        @Test
        @DisplayName("성공: IS_TITLE 일 시")
        void getKeywordConditionWhenIS_TITLE() {
            //given
            KeywordFilter keywordFilter = KeywordFilter.IS_TITLE;

            //when
            KeywordCondition keywordCondition = keywordFilter.getKeywordCondition();

            //then
            assertThat(keywordCondition.titleFilter()).isTrue();
            assertThat(keywordCondition.contentFilter()).isFalse();
            assertThat(keywordCondition.shelterNameFilter()).isFalse();
        }

        @Test
        @DisplayName("성공: IS_CONTENT 일 시")
        void getKeywordConditionWhenIS_CONTENT() {
            //given
            KeywordFilter keywordFilter = KeywordFilter.IS_CONTENT;

            //when
            KeywordCondition keywordCondition = keywordFilter.getKeywordCondition();

            //then
            assertThat(keywordCondition.titleFilter()).isFalse();
            assertThat(keywordCondition.contentFilter()).isTrue();
            assertThat(keywordCondition.shelterNameFilter()).isFalse();
        }

        @Test
        @DisplayName("성공: IS_SHELTER_NAME 일 시")
        void getKeywordConditionWhenIS_SHELTER_NAME() {
            //given
            KeywordFilter keywordFilter = KeywordFilter.IS_SHELTER_NAME;

            //when
            KeywordCondition keywordCondition = keywordFilter.getKeywordCondition();

            //then
            assertThat(keywordCondition.titleFilter()).isFalse();
            assertThat(keywordCondition.contentFilter()).isFalse();
            assertThat(keywordCondition.shelterNameFilter()).isTrue();
        }

        @Test
        @DisplayName("성공: ALL 일 시")
        void getKeywordConditionWhenALL() {
            //given
            KeywordFilter keywordFilter = KeywordFilter.ALL;

            //when
            KeywordCondition keywordCondition = keywordFilter.getKeywordCondition();

            //then
            assertThat(keywordCondition.titleFilter()).isTrue();
            assertThat(keywordCondition.contentFilter()).isTrue();
            assertThat(keywordCondition.shelterNameFilter()).isTrue();
        }
    }

    @Nested
    @DisplayName("getKeywordConditionByShelter 메서드 호출 시")
    class GetKeywordConditionByShelterTest {

        @Test
        @DisplayName("성공: IS_TITLE 일 시")
        void getKeywordConditionByShelterWhenIS_TITLE() {
            //given
            KeywordFilter keywordFilter = KeywordFilter.IS_TITLE;

            //when
            KeywordConditionByShelter keywordConditionByShelter = keywordFilter.getKeywordConditionByShelter();

            //then
            assertThat(keywordConditionByShelter.titleFilter()).isTrue();
            assertThat(keywordConditionByShelter.contentFilter()).isFalse();
        }

        @Test
        @DisplayName("성공: IS_CONTENT 일 시")
        void getKeywordConditionByShelterWhenIS_CONTENT() {
            //given
            KeywordFilter keywordFilter = KeywordFilter.IS_CONTENT;

            //when
            KeywordConditionByShelter keywordConditionByShelter = keywordFilter.getKeywordConditionByShelter();

            //then
            assertThat(keywordConditionByShelter.titleFilter()).isFalse();
            assertThat(keywordConditionByShelter.contentFilter()).isTrue();
        }

        @Test
        @DisplayName("성공: ALL 일 시")
        void getKeywordConditionByShelterWhenALL() {
            //given
            KeywordFilter keywordFilter = KeywordFilter.ALL;

            //when
            KeywordConditionByShelter keywordConditionByShelter = keywordFilter.getKeywordConditionByShelter();

            //then
            assertThat(keywordConditionByShelter.titleFilter()).isTrue();
            assertThat(keywordConditionByShelter.contentFilter()).isTrue();
        }
    }
}
