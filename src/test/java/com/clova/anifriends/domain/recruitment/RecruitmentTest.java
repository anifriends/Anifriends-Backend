package com.clova.anifriends.domain.recruitment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RecruitmentTest {

    @Nested
    @DisplayName("Recruitment 생성 시")
    class NewRecruitmentTest {

        Shelter shelter = ShelterFixture.shelter();
        String title = "title";
        int capacity = 10;
        String content = "content";
        LocalDateTime now = LocalDateTime.now();
        List<String> imageUrls = List.of("imageUrl1", "imageUrl2");
        LocalDateTime startTime = now.plusHours(1);
        LocalDateTime endTime = startTime.plusMinutes(1);
        LocalDateTime deadline = now.plusMinutes(10);

        @Test
        @DisplayName("성공")
        void newRecruitment() {
            //given
            //when
            Recruitment recruitment = new Recruitment(
                shelter, title, capacity, content, startTime, endTime, deadline, imageUrls);

            //then
            assertThat(recruitment.getTitle()).isEqualTo(title);
            assertThat(recruitment.getContent()).isEqualTo(content);
            assertThat(recruitment.getCapacity()).isEqualTo(capacity);
            assertThat(recruitment.getStartTime()).isEqualTo(startTime);
            assertThat(recruitment.getEndTime()).isEqualTo(endTime);
            assertThat(recruitment.getDeadline()).isEqualTo(deadline);
            assertThat(recruitment.isClosed()).isFalse();
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): 이미지 url 리스트 사이즈가 5를 초과")
        void exceptionWhenImageUrlsSizeOver5() {
            //given
            List<String> imageUrlsOver5 = List.of("www.aws.s3.com/2", "www.aws.s3.com/2",
                "www.aws.s3.com/2", "www.aws.s3.com/2", "www.aws.s3.com/2", "www.aws.s3.com/2");

            //when
            Exception exception = catchException(() -> new Recruitment(
                shelter, title, capacity, content, startTime, endTime, deadline, imageUrlsOver5));

            //then
            assertThat(exception).isInstanceOf(RecruitmentBadRequestException.class);
        }
    }
}
