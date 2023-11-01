package com.clova.anifriends.domain.recruitment;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RecruitmentTest {

    @Nested
    @DisplayName("Recruitment 생성 시")
    class NewRecruitmentTest {

        @Test
        @DisplayName("성공")
        void newRecruitment() {
            //given
            String title = "title";
            int capacity = 10;
            String content = "content";
            LocalDateTime now = LocalDateTime.now();
            List<String> imageUrls = List.of("imageUrl1", "imageUrl2");
            LocalDateTime startTime = now.plusHours(1);
            LocalDateTime endTime = startTime.minusMinutes(1);
            LocalDateTime deadline = now.plusMinutes(10);

            //when
            Recruitment recruitment = new Recruitment(
                null, title, capacity, content, startTime, endTime, deadline, imageUrls);

            //then
            assertThat(recruitment.getTitle()).isEqualTo(title);
            assertThat(recruitment.getContent()).isEqualTo(content);
            assertThat(recruitment.getCapacity()).isEqualTo(capacity);
            assertThat(recruitment.getStartTime()).isEqualTo(startTime);
            assertThat(recruitment.getEndTime()).isEqualTo(endTime);
            assertThat(recruitment.getDeadline()).isEqualTo(deadline);
            assertThat(recruitment.isClosed()).isFalse();
        }
    }
}