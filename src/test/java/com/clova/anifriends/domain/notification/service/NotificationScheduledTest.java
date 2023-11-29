package com.clova.anifriends.domain.notification.service;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import com.clova.anifriends.base.BaseIntegrationTest;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;

class NotificationScheduledTest extends BaseIntegrationTest {

    @SpyBean
    private VolunteerNotificationService volunteerNotificationService;

    @SpyBean
    private ShelterNotificationService shelterNotificationService;

    @Test
    @DisplayName("notifyADayBeforeVolunteer 메서드 실행 시")
    void notifyADayBeforeVolunteer() {
        Awaitility.await()
            .atMost(3, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                verify(volunteerNotificationService, atLeastOnce()).notifyADayBeforeVolunteer();
            });
    }

    @Test
    @DisplayName("notifyThreeDayBeforeVolunteer 메서드 실행 시")
    void notifyThreeDayBeforeVolunteer() {
        Awaitility.await()
            .atMost(3, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                verify(volunteerNotificationService,
                    atLeastOnce()).notifyThreeDayBeforeVolunteer();
            });
    }

    @Test
    @DisplayName("notifyEncourageWriteReview 메서드 실행 시")
    void notifyEncourageWriteReview() {
        Awaitility.await()
            .atMost(3, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                verify(volunteerNotificationService,
                    atLeastOnce()).notifyEncourageWriteReview();
            });
    }

    @Test
    @DisplayName("notifyEncourageCheckAttendance 메서드 실행 시")
    void notifyEncourageCheckAttendance() {
        Awaitility.await()
            .atMost(3, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                verify(shelterNotificationService,
                    atLeastOnce()).notifyEncourageCheckAttendance();
            });
    }
}