package com.clova.anifriends.domain.notification;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.domain.notification.vo.NotificationType;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VolunteerNotificationTest {

    @Nested
    @DisplayName("VolunteerNotification 생성 시")
    class NewVolunteerNotificationTest {

        Volunteer volunteer = VolunteerFixture.volunteer();
        String title = "title";
        String content = "content";
        String type = "VOLUNTEER_APPROVED";

        @Test
        @DisplayName("성공")
        void newVolunteerNotification() {
            // given
            // when
            VolunteerNotification volunteerNotification = new VolunteerNotification(
                volunteer, title, content, type);

            // then
            assertThat(volunteerNotification.getVolunteer()).isEqualTo(volunteer);
            assertThat(volunteerNotification.getTitle()).isEqualTo(title);
            assertThat(volunteerNotification.getContent()).isEqualTo(content);
            assertThat(volunteerNotification.getType()).isEqualTo(NotificationType.valueOf(type));
        }
    }
}
