package com.clova.anifriends.domain.volunteer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VolunteerImageTest {

    @Nested
    @DisplayName("VolunteerImage 생성 시")
    class NewVolunteerImageTest {

        @Test
        @DisplayName("성공")
        void newVolunteer() {
            //given
            String imageUrl = "www.aws.s3.com/2";

            //when
            VolunteerImage volunteerImage = new VolunteerImage(null, imageUrl);

            //then
            assertThat(volunteerImage.getImageUrl()).isEqualTo(imageUrl);
        }
    }
}