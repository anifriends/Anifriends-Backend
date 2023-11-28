package com.clova.anifriends.domain.notification.service;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class NotificationScheduledTest {

    @SpyBean
    private VolunteerNotificationService volunteerNotificationService;


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
}
