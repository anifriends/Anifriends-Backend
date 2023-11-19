package com.clova.anifriends.domain.recruitment;

import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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

    @Nested
    @DisplayName("closeRecruitment 메서드 호출 시")
    class CloseRecruitmentTest {

        @Test
        @DisplayName("성공")
        void closeRecruitment() {
            //given
            Shelter shelter = ShelterFixture.shelter();
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);

            //when
            recruitment.closeRecruitment();

            //
            assertThat(recruitment.isClosed()).isTrue();
        }
    }

    @Nested
    @DisplayName("updateRecruitment 메서드 호출 시")
    class UpdateRecruitmentTest {

        Recruitment recruitment;

        @BeforeEach
        void setUp() {
            Shelter shelter = ShelterFixture.shelter();
            recruitment = RecruitmentFixture.recruitment(shelter);
        }

        @Test
        @DisplayName("성공 모든 인자가 null 아닐 때")
        void updateRecruitment() {
            //given
            String newTitle = recruitment.getTitle() + "a";
            LocalDateTime newStartTime = recruitment.getStartTime().plusDays(1);
            LocalDateTime newEndTime = recruitment.getEndTime().plusDays(1);
            LocalDateTime newDeadline = recruitment.getDeadline().plusDays(1);
            int newCapacity = recruitment.getCapacity() + 1;
            String newContent = recruitment.getContent() + "a";
            List<String> newImageUrls = List.of("a1", "a2", "a3", "a4");

            //when
            recruitment.updateRecruitment(newTitle, newStartTime, newEndTime, newDeadline,
                newCapacity, newContent, newImageUrls);

            //then
            assertThat(recruitment.getTitle()).isEqualTo(newTitle);
            assertThat(recruitment.getStartTime()).isEqualTo(newStartTime);
            assertThat(recruitment.getEndTime()).isEqualTo(newEndTime);
            assertThat(recruitment.getDeadline()).isEqualTo(newDeadline);
            assertThat(recruitment.getCapacity()).isEqualTo(newCapacity);
            assertThat(recruitment.getContent()).isEqualTo(newContent);
            assertThat(recruitment.getImages()).isEqualTo(newImageUrls);
        }

        @Test
        @DisplayName("성공: 모든 인자가 null 일 때")
        void updateRecruitmentWhenNullArguments() {
            //given
            String givenTitle = recruitment.getTitle();
            LocalDateTime givenStartTime = recruitment.getStartTime();
            LocalDateTime givenEndTime = recruitment.getEndTime();
            LocalDateTime givenDeadline = recruitment.getDeadline();
            int givenCapacity = recruitment.getCapacity();
            String givenContent = recruitment.getContent();
            List<String> givenImageUrls = recruitment.getImages();

            //when
            recruitment.updateRecruitment(null, null, null, null,
                null, null, null);

            //then
            assertThat(recruitment.getTitle()).isEqualTo(givenTitle);
            assertThat(recruitment.getStartTime()).isEqualTo(givenStartTime);
            assertThat(recruitment.getEndTime()).isEqualTo(givenEndTime);
            assertThat(recruitment.getDeadline()).isEqualTo(givenDeadline);
            assertThat(recruitment.getCapacity()).isEqualTo(givenCapacity);
            assertThat(recruitment.getContent()).isEqualTo(givenContent);
            assertThat(recruitment.getImages()).containsExactlyElementsOf(givenImageUrls);
        }

        @Test
        @DisplayName("성공: 이미지 리스트가 비어 있을 때, 모든 이미지를 삭제한다.")
        void updateRecruitmentWhenEmptyImageUrls() {
            //given
            List<String> emptyImageUrls = List.of();

            //when
            recruitment.updateRecruitment(null, null, null, null,
                null, null, emptyImageUrls);

            //then
            assertThat(recruitment.getImages()).isEmpty();
        }

        @Test
        @DisplayName("예외(RecruitmentBadRequestException): 이미지 리스트가 5장을 초과했을 때")
        void exceptionWhenImageUrlsSizeOver5() {
            //given
            List<String> imageUrlsOver5 = List.of("a1", "a2", "a3", "a4", "a5", "a6");

            //when
            Exception exception = catchException(
                () -> recruitment.updateRecruitment(null, null, null, null,
                    null, null, imageUrlsOver5));

            //then
            assertThat(exception).isInstanceOf(RecruitmentBadRequestException.class);
        }
    }

    @Nested
    @DisplayName("findImagesToDelete 메서드 호출 시")
    class FindImagesToDelete {

        @Test
        @DisplayName("성공: 기존 이미지 2개, 유지 이미지 0개, 새로운 이미지 0개")
        void findImagesToDelete1() {
            // given
            String imageUrl1 = "www.aws.s3.com/1";
            String imageUrl2 = "www.aws.s3.com/2";
            List<String> existsImageUrls = List.of(imageUrl1, imageUrl2);

            Recruitment recruitment = RecruitmentFixture
                .recruitmentWithImages(shelter(), existsImageUrls);

            List<String> newImageUrls = List.of();

            // when
            List<String> result = recruitment.findImagesToDelete(newImageUrls);

            // then
            assertThat(result).isEqualTo(existsImageUrls);
        }

        @Test
        @DisplayName("성공: 기존 이미지 0개, 유지 이미지 0개, 새로운 이미지 0개")
        void findImagesToDelete2() {
            // given
            List<String> existsImageUrls = List.of();

            Recruitment recruitment = RecruitmentFixture
                .recruitmentWithImages(shelter(), existsImageUrls);

            List<String> newImageUrls = List.of();

            // when
            List<String> result = recruitment.findImagesToDelete(newImageUrls);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("성공: 기존 이미지 2개, 유지 이미지 1개, 새로운 이미지 1개")
        void findImagesToDelete3() {
            // given
            String imageUrl1 = "www.aws.s3.com/1";
            String imageUrl2 = "www.aws.s3.com/2";
            List<String> existsImageUrls = List.of(imageUrl1, imageUrl2);

            Recruitment recruitment = RecruitmentFixture
                .recruitmentWithImages(shelter(), existsImageUrls);

            List<String> newImageUrls = List.of(imageUrl2);

            // when
            List<String> result = recruitment.findImagesToDelete(newImageUrls);

            // then
            assertThat(result).isEqualTo(List.of(imageUrl1));
        }
    }
}
