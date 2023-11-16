package com.clova.anifriends.domain.notification.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.notification.ShelterNotification;
import com.clova.anifriends.domain.notification.support.fixture.ShelterNotificationFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDateTime;
import java.util.List;
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
            ShelterNotification notification1 = ShelterNotificationFixture.shelterNotification(shelter);
            ShelterNotification notification2 = ShelterNotificationFixture.shelterNotification(shelter);
            ReflectionTestUtils.setField(notification1, "createdAt", LocalDateTime.now().minusDays(3));
            ReflectionTestUtils.setField(notification2, "createdAt", LocalDateTime.now().minusDays(1));

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
}
