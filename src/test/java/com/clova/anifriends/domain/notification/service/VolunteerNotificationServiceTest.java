package com.clova.anifriends.domain.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.clova.anifriends.domain.notification.VolunteerNotification;
import com.clova.anifriends.domain.notification.dto.response.FindVolunteerHasNewNotificationResponse;
import com.clova.anifriends.domain.notification.dto.response.FindVolunteerNotificationsResponse;
import com.clova.anifriends.domain.notification.repository.VolunteerNotificationRepository;
import com.clova.anifriends.domain.notification.support.fixture.VolunteerNotificationFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
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
class VolunteerNotificationServiceTest {

    @InjectMocks
    VolunteerNotificationService volunteerNotificationService;

    @Mock
    VolunteerNotificationRepository volunteerNotificationRepository;

    @Nested
    @DisplayName("findVolunteerNotifications 메서드 실행 시")
    class FindVolunteerNotificationsTest {

        @Test
        @DisplayName("성공")
        void findVolunteerNotifications() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            ReflectionTestUtils.setField(volunteer, "volunteerId", 1L);
            VolunteerNotification volunteerNotification = VolunteerNotificationFixture.volunteerNotification(
                volunteer);
            FindVolunteerNotificationsResponse expected = FindVolunteerNotificationsResponse.from(
                List.of(volunteerNotification));
            given(volunteerNotificationRepository.findByVolunteer_VolunteerIdOrderByCreatedAtDesc(
                anyLong()))
                .willReturn(List.of(volunteerNotification));

            // when
            FindVolunteerNotificationsResponse result = volunteerNotificationService.findVolunteerNotifications(
                volunteer.getVolunteerId());

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("findVolunteerHasNewNotification 메서드 실행 시")
    class FindVolunteerHasNewNotificationTest {

        @Test
        @DisplayName("성공")
        void findVolunteerHasNewNotification() {
            // given
            FindVolunteerHasNewNotificationResponse expected = FindVolunteerHasNewNotificationResponse.from(true);
            given(volunteerNotificationRepository.hasNewNotification(anyLong())).willReturn(true);

            // when
            FindVolunteerHasNewNotificationResponse result = volunteerNotificationService.findVolunteerHasNewNotification(
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
            Volunteer volunteer = VolunteerFixture.volunteer();
            ReflectionTestUtils.setField(volunteer, "volunteerId", 1L);

            // when
            volunteerNotificationService.updateNotificationRead(volunteer.getVolunteerId());

            // then
            verify(volunteerNotificationRepository, times(1))
                .updateBulkRead(volunteer.getVolunteerId());
        }
    }
}
