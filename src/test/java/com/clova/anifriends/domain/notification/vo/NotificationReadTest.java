package com.clova.anifriends.domain.notification.vo;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.clova.anifriends.domain.notification.exception.NotificationBadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotificationReadTest {

    @Nested
    @DisplayName("NotificationRead 생성 시")
    class NewNotificationReadTest {

        @Test
        @DisplayName("성공")
        void newNotificationRead() {
            //given
            Boolean isRead = true;

            //when
            NotificationRead notificationRead = new NotificationRead(isRead);

            //then
            Assertions.assertThat(notificationRead.getIsRead()).isEqualTo(isRead);
        }

        @Test
        @DisplayName("예외(NotificationBadRequestException): 입력값이 null")
        void exceptionWhenIsReadIsNull() {
            //given
            Boolean isRead = null;

            //when
            Exception exception = assertThrows(NotificationBadRequestException.class,
                () -> new NotificationRead(isRead));

            //then
            Assertions.assertThat(exception).isInstanceOf(NotificationBadRequestException.class);
        }
    }
}
