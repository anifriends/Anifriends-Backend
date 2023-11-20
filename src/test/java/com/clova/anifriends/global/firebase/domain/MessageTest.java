package com.clova.anifriends.global.firebase.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.domain.notification.wrapper.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MessageTest {

    @Nested
    @DisplayName("Message 생성 시")
    class NewMessageTest {

        String token = "token";
        Notification notification = new Notification("title", "content", NotificationType.NEW_APPLICANT);

        @Test
        @DisplayName("성공")
        void newMessage() {
            // given
            // when
            Message message = new Message(token, notification);

            // then
            assertThat(message.getToken()).isEqualTo(token);
            assertThat(message.getNotification()).isEqualTo(notification);
        }
    }
}