package com.clova.anifriends.domain.notification.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.clova.anifriends.domain.notification.exception.NotificationBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotificationContentTest {

    @Nested
    @DisplayName("NotificationContent 생성 시")
    class NewNotificationContentTest {

        @Test
        @DisplayName("성공")
        void newNotificationContent() {
            //given
            String content = "content";

            //when
            NotificationContent notificationContent = new NotificationContent(content);

            //then
            assertThat(notificationContent.getContent()).isEqualTo(content);
        }

        @Test
        @DisplayName("예외(NotificationBadRequestException): 입력값이 null")
        void exceptionWhenContentIsNull() {
            //given
            String nullContent = null;

            //when
            Exception exception = assertThrows(NotificationBadRequestException.class, () -> new NotificationContent(nullContent));

            //then
            assertThat(exception).isInstanceOf(NotificationBadRequestException.class);
        }
    }
}
