package com.clova.anifriends.global.firebase.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.domain.notification.wrapper.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FCMMessageTest {

    @Nested
    @DisplayName("FCMMessage 생성 시")
    class NewFCMMessageTest {

        String targetToken = "targetToken";
        String title = "title";
        String content = "content";
        NotificationType type = NotificationType.NEW_APPLICANT;

        @Test
        @DisplayName("성공")
        void newFCMMessage() {
            // given
            // when
            FCMMessage fcmMessage = new FCMMessage(false,
                Message.of(targetToken, title, content, type));

            // then
            assertThat(fcmMessage.isValidateOnly()).isFalse();
            assertThat(fcmMessage.getMessage().getToken()).isEqualTo(targetToken);
            assertThat(fcmMessage.getMessage().getNotification().getTitle()).isEqualTo(title);
            assertThat(fcmMessage.getMessage().getNotification().getContent()).isEqualTo(content);
            assertThat(fcmMessage.getMessage().getNotification().getType()).isEqualTo(type);
        }

    }
}