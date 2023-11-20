package com.clova.anifriends.domain.notification.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.clova.anifriends.domain.notification.exception.NotificationBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotificationTitleTest {

    @Nested
    @DisplayName("NotificationTitle 생성 시")
    class NewNotificationTitleTest {

        @Test
        @DisplayName("성공")
        void newNotificationTitle() {
            //given
            String title = "title";

            //when
            NotificationTitle notificationTitle = new NotificationTitle(title);

            //then
            assertThat(notificationTitle.getTitle()).isEqualTo(title);
        }

        @Test
        @DisplayName("예외(NotificationBadRequestException): 입력값이 null")
        void exceptionWhenTitleIsNull() {
            //given
            String nullTitle = null;

            //when
            Exception exception = assertThrows(NotificationBadRequestException.class, () -> new NotificationTitle(nullTitle));

            //then
            assertThat(exception).isInstanceOf(NotificationBadRequestException.class);
        }
    }
}
