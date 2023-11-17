package com.clova.anifriends.domain.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.clova.anifriends.domain.notification.ShelterNotification;
import com.clova.anifriends.domain.notification.dto.response.FindShelterHasNewNotificationResponse;
import com.clova.anifriends.domain.notification.dto.response.FindShelterNotificationsResponse;
import com.clova.anifriends.domain.notification.repository.ShelterNotificationRepository;
import com.clova.anifriends.domain.notification.support.fixture.ShelterNotificationFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ShelterNotificationServiceTest {

    @InjectMocks
    ShelterNotificationService shelterNotificationService;

    @Mock
    ShelterNotificationRepository shelterNotificationRepository;

    @Nested
    @DisplayName("findShelterNotifications 메서드 실행 시")
    class FindShelterNotificationsTest {

        @Test
        @DisplayName("성공")
        void findShelterNotifications() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            ReflectionTestUtils.setField(shelter, "shelterId", 1L);
            ShelterNotification shelterNotification = ShelterNotificationFixture.shelterNotification(
                shelter);
            FindShelterNotificationsResponse expected = FindShelterNotificationsResponse.from(
                List.of(shelterNotification));
            given(shelterNotificationRepository.findByShelter_ShelterIdOrderByCreatedAtDesc(
                anyLong()))
                .willReturn(List.of(shelterNotification));

            // when
            FindShelterNotificationsResponse result = shelterNotificationService.findShelterNotifications(
                shelter.getShelterId());

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("findShelterHasNewNotification 메서드 실행 시")
    class FindShelterHasNewNotificationTest {

        @Test
        @DisplayName("성공")
        void findShelterHasNewNotification() {
            // given
            FindShelterHasNewNotificationResponse expected = FindShelterHasNewNotificationResponse.from(
                true);
            given(shelterNotificationRepository.hasNewNotification(anyLong())).willReturn(true);

            // when
            FindShelterHasNewNotificationResponse result = shelterNotificationService.findShelterHasNewNotification(
                1L);

            // then
            assertThat(result).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("updateNotificationRead 메서드 실행 시")
    class UpdateNotificationReadTest {

        @Test
        @DisplayName("성공")
        void updateNotificationRead() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            ReflectionTestUtils.setField(shelter, "shelterId", 1L);

            // when
            shelterNotificationService.updateNotificationRead(shelter.getShelterId());

            // then
            verify(shelterNotificationRepository, times(1))
                .updateBulkRead(shelter.getShelterId());
        }
    }
}
