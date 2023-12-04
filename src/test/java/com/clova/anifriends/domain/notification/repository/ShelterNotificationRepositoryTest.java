package com.clova.anifriends.domain.notification.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.notification.ShelterNotification;
import com.clova.anifriends.domain.notification.support.fixture.ShelterNotificationFixture;
import com.clova.anifriends.domain.notification.vo.NotificationRead;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class ShelterNotificationRepositoryTest extends BaseRepositoryTest {

    @Nested
    @DisplayName("findByShelter_ShelterIdOrderByCreatedAtDesc 실행 시")
    class FindByShelter_ShelterIdOrderByCreatedAtDescTest {

        @Test
        @DisplayName("성공: 생성일 내림차순 조회")
        void findByShelter_ShelterIdOrderByCreatedAtDesc() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            ShelterNotification notification1 = ShelterNotificationFixture.shelterNotification(
                shelter);
            ShelterNotification notification2 = ShelterNotificationFixture.shelterNotification(
                shelter);
            ReflectionTestUtils.setField(notification1, "createdAt",
                LocalDateTime.now().minusDays(3));
            ReflectionTestUtils.setField(notification2, "createdAt",
                LocalDateTime.now().minusDays(1));

            shelterRepository.save(shelter);
            shelterNotificationRepository.save(notification1);
            shelterNotificationRepository.save(notification2);

            // when
            List<ShelterNotification> expected = shelterNotificationRepository.findByShelter_ShelterIdOrderByCreatedAtDesc(
                shelter.getShelterId());

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
            Shelter shelter = ShelterFixture.shelter();
            ShelterNotification notification1 = ShelterNotificationFixture.shelterNotification(
                shelter);
            ShelterNotification notification2 = ShelterNotificationFixture.shelterNotification(
                shelter);
            NotificationRead notificationRead = new NotificationRead(true);
            ReflectionTestUtils.setField(notification1, "isRead", notificationRead);
            ReflectionTestUtils.setField(notification2, "isRead", notificationRead);

            shelterRepository.save(shelter);
            shelterNotificationRepository.save(notification1);
            shelterNotificationRepository.save(notification2);

            // when
            boolean expected = shelterNotificationRepository.hasNewNotification(
                shelter.getShelterId());

            // then
            assertThat(expected).isFalse();
        }

        @Test
        @DisplayName("성공: 안 읽은 알림이 있는 경우")
        void hasNewNotificationTrue() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            ShelterNotification notification1 = ShelterNotificationFixture.shelterNotification(
                shelter);
            ShelterNotification notification2 = ShelterNotificationFixture.shelterNotification(
                shelter);
            NotificationRead notificationRead = new NotificationRead(true);
            ReflectionTestUtils.setField(notification1, "isRead", notificationRead);

            shelterRepository.save(shelter);
            shelterNotificationRepository.save(notification1);
            shelterNotificationRepository.save(notification2);

            // when
            boolean expected = shelterNotificationRepository.hasNewNotification(
                shelter.getShelterId());

            // then
            assertThat(expected).isTrue();
        }
    }

    @Nested
    @DisplayName("updateBulkRead 실행 시")
    class UpdateBulkReadTest {

        @Test
        @DisplayName("성공")
        void updateBulkRead() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            ShelterNotification notification1 = ShelterNotificationFixture.shelterNotification(
                shelter);
            ShelterNotification notification2 = ShelterNotificationFixture.shelterNotification(
                shelter);

            shelterRepository.save(shelter);
            shelterNotificationRepository.save(notification1);
            shelterNotificationRepository.save(notification2);

            // when
            shelterNotificationRepository.updateBulkRead(shelter.getShelterId());

            // then
            Optional<ShelterNotification> found1 = shelterNotificationRepository.findById(
                notification1.getShelterNotificationId());
            Optional<ShelterNotification> found2 = shelterNotificationRepository.findById(
                notification2.getShelterNotificationId());
            assertThat(found1.get().getIsRead()).isTrue();
            assertThat(found2.get().getIsRead()).isTrue();
        }
    }
}
