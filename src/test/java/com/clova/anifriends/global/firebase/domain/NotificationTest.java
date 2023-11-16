package com.clova.anifriends.global.firebase.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.domain.notification.wrapper.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Nested
    @DisplayName("Notification 생성 시")
    class NewNotificationTest {

        String title = "title";
        String content = "content";
        NotificationType type = NotificationType.NEW_APPLICANT;

        @Test
        @DisplayName("성공")
        void newNotification() {
            //given
            //when
            Notification notification = new Notification(title, content, type);

            //then
            assertThat(notification.getTitle()).isEqualTo(title);
            assertThat(notification.getContent()).isEqualTo(content);
            assertThat(notification.getType()).isEqualTo(type);
        }
    }

}