package com.clova.anifriends.domain.notification;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.domain.notification.vo.NotificationType;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ShelterNotificationTest {

    @Nested
    @DisplayName("ShelterNotification 생성 시")
    class NewShelterNotificationTest {

        Shelter shelter = ShelterFixture.shelter();
        String title = "title";
        String content = "content";
        String type = "NEW_APPLICANT";

        @Test
        @DisplayName("성공")
        void newShelterNotification() {
            // given
            // when
            ShelterNotification shelterNotification = new ShelterNotification(
                shelter, title, content, type);

            // then
            assertThat(shelterNotification.getShelter()).isEqualTo(shelter);
            assertThat(shelterNotification.getTitle()).isEqualTo(title);
            assertThat(shelterNotification.getContent()).isEqualTo(content);
            assertThat(shelterNotification.getType()).isEqualTo(NotificationType.valueOf(type));
        }
    }
}
