package com.clova.anifriends.domain.shelter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ShelterImageTest {

    @Nested
    @DisplayName("ShelterImage 생성 시")
    class NewShelterImageTest {

        @Test
        @DisplayName("성공")
        void newShelterImage() {
            //given
            String imageUrl = "www.aws.s3.com/2";

            //when
            ShelterImage shelterImage = new ShelterImage(null, imageUrl);

            //then
            assertThat(shelterImage.getImageUrl()).isEqualTo(imageUrl);
        }
    }
}