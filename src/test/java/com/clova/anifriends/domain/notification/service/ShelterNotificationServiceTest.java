package com.clova.anifriends.domain.notification.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.clova.anifriends.domain.notification.ShelterNotification;
import com.clova.anifriends.domain.notification.dto.response.FindShelterNotificationsResponse;
import com.clova.anifriends.domain.notification.repository.ShelterNotificationRepository;
import com.clova.anifriends.domain.notification.support.fixture.ShelterNotificationFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.util.List;
import org.assertj.core.api.Assertions;
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
            ShelterNotification shelterNotification = ShelterNotificationFixture.shelterNotification(shelter);
            FindShelterNotificationsResponse expected = FindShelterNotificationsResponse.from(List.of(shelterNotification));
            given(shelterNotificationRepository.findByShelter_ShelterIdOrderByCreatedAtDesc(anyLong()))
                .willReturn(List.of(shelterNotification));

            // when
            FindShelterNotificationsResponse result = shelterNotificationService.findShelterNotifications(shelter.getShelterId());

            // then
            Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }
    }
}