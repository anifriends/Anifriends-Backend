package com.clova.anifriends.domain.notification.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.notification.VolunteerNotification;
import com.clova.anifriends.domain.notification.support.fixture.VolunteerNotificationFixture;
import com.clova.anifriends.domain.notification.vo.NotificationRead;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class VolunteerNotificationRepositoryTest extends BaseRepositoryTest {

    @Nested
    @DisplayName("findByVolunteer_VolunteerIdOrderByCreatedAtDesc 실행 시")
    class FindByVolunteer_VolunteerIdOrderByCreatedAtDescTest {

        @Test
        @DisplayName("성공: 생성일 내림차순 조회")
        void findByVolunteer_VolunteerIdOrderByCreatedAtDesc() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            VolunteerNotification notification1 = VolunteerNotificationFixture.volunteerNotification(
                volunteer);
            VolunteerNotification notification2 = VolunteerNotificationFixture.volunteerNotification(
                volunteer);
            ReflectionTestUtils.setField(notification1, "createdAt",
                LocalDateTime.now().minusDays(3));
            ReflectionTestUtils.setField(notification2, "createdAt",
                LocalDateTime.now().minusDays(1));

            volunteerRepository.save(volunteer);
            volunteerNotificationRepository.save(notification1);
            volunteerNotificationRepository.save(notification2);

            // when
            List<VolunteerNotification> expected = volunteerNotificationRepository.findByVolunteer_VolunteerIdOrderByCreatedAtDesc(
                volunteer.getVolunteerId());

            // then
            assertThat(expected.get(0)).isEqualTo(notification2);
            assertThat(expected.get(1)).isEqualTo(notification1);
        }
    }

    @Nested
    @DisplayName("hasNewNotification 실행 시")
    class HasNewNotificationTest {

        @Test
        @DisplayName("성공: 안 읽은 알림이 없는 경우")
        void hasNewNotificationFalse() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            VolunteerNotification notification1 = VolunteerNotificationFixture.volunteerNotification(volunteer);
            VolunteerNotification notification2 = VolunteerNotificationFixture.volunteerNotification(volunteer);
            NotificationRead notificationRead = new NotificationRead(true);
            ReflectionTestUtils.setField(notification1, "isRead", notificationRead);
            ReflectionTestUtils.setField(notification2, "isRead", notificationRead);

            volunteerRepository.save(volunteer);
            volunteerNotificationRepository.save(notification1);
            volunteerNotificationRepository.save(notification2);

            // when
            boolean expected = volunteerNotificationRepository.hasNewNotification(volunteer.getVolunteerId());

            // then
            assertThat(expected).isFalse();
        }

        @Test
        @DisplayName("성공: 안 읽은 알림이 있는 경우")
        void hasNewNotificationTrue() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            VolunteerNotification notification1 = VolunteerNotificationFixture.volunteerNotification(volunteer);
            VolunteerNotification notification2 = VolunteerNotificationFixture.volunteerNotification(volunteer);
            NotificationRead notificationRead = new NotificationRead(true);
            ReflectionTestUtils.setField(notification2, "isRead", notificationRead);

            volunteerRepository.save(volunteer);
            volunteerNotificationRepository.save(notification1);
            volunteerNotificationRepository.save(notification2);

            // when
            boolean expected = volunteerNotificationRepository.hasNewNotification(volunteer.getVolunteerId());

            // then
            assertThat(expected).isTrue();
        }
    }

    @Nested
    @DisplayName("updateBulkRead")
    class UpdateBulkReadTest {

        @Test
        @DisplayName("성공: 읽음 처리")
        void updateBulkRead() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            VolunteerNotification notification1 = VolunteerNotificationFixture.volunteerNotification(
                volunteer);
            VolunteerNotification notification2 = VolunteerNotificationFixture.volunteerNotification(
                volunteer);

            volunteerRepository.save(volunteer);
            volunteerNotificationRepository.save(notification1);
            volunteerNotificationRepository.save(notification2);

            // when
            volunteerNotificationRepository.updateBulkRead(volunteer.getVolunteerId());

            // then
            Optional<VolunteerNotification> found1 = volunteerNotificationRepository.findById(
                notification1.getVolunteerNotificationId());
            Optional<VolunteerNotification> found2 = volunteerNotificationRepository.findById(
                notification2.getVolunteerNotificationId());
            assertThat(found1.get().getIsRead()).isTrue();
            assertThat(found2.get().getIsRead()).isTrue();
        }
    }
}
