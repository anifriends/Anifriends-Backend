package com.clova.anifriends.domain.animal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AnimalImageTest {

    @Nested
    @DisplayName("AnimalImage 생성 시")
    class NewAnimalImageTest {

        @Test
        @DisplayName("성공")
        void newAnimalImage() {
            //given
            String imageUrl = "www.aws.s3.com/2";

            //when
            AnimalImage animalImage = new AnimalImage(null, imageUrl);

            //then
            assertThat(animalImage.getImageUrl()).isEqualTo(imageUrl);
        }
    }
}