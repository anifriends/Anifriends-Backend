package com.clova.anifriends.domain.volunteer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VolunteerImageTest {

    @Nested
    @DisplayName("VolunteerImage 생성 시")
    class newVolunteerImageTest {

        Volunteer volunteer;
        String imageUrl;

        @Test
        @DisplayName("성공")
        void success() {
            // given
            volunteer = VolunteerFixture.volunteer();
            imageUrl = "url";

            // when
            VolunteerImage volunteerImage = new VolunteerImage(volunteer, imageUrl);

            // then
            assertThat(volunteerImage.getImageUrl()).isEqualTo(imageUrl);
        }

        @Test
        @DisplayName("예외: imageUrl이 null")
        void throwExceptionWhenImageUrlIsNull() {
            // given
            volunteer = VolunteerFixture.volunteer();

            // when
            Exception exception = catchException(
                () -> new VolunteerImage(volunteer, imageUrl)
            );

            // then
            assertThat(exception).isInstanceOf(VolunteerBadRequestException.class);
        }

        @Test
        @DisplayName("예외: volunteer가 null인 경우")
        void throwExceptionWhenVolunteerIsNull() {
            // given
            imageUrl = "url";

            // when
            Exception exception = catchException(
                () -> new VolunteerImage(volunteer, imageUrl)
            );

            // then
            assertThat(exception).isInstanceOf(VolunteerBadRequestException.class);
        }
    }
}
