package com.clova.anifriends.global.scheduler;

import com.clova.anifriends.domain.notification.service.ShelterNotificationService;
import com.clova.anifriends.domain.notification.service.VolunteerNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotifyScheduler {

    private final VolunteerNotificationService volunteerNotificationService;
    private final ShelterNotificationService shelterNotificationService;

    @Scheduled(cron = "${schedules.cron.notification.a-day-before-volunteer}")
    public void notifyADayBeforeVolunteer() {
        volunteerNotificationService.notifyADayBeforeVolunteer();
    }

    @Scheduled(cron = "${schedules.cron.notification.three-day-before-volunteer}")
    public void notifyThreeDayBeforeVolunteer() {
        volunteerNotificationService.notifyThreeDayBeforeVolunteer();
    }

    @Scheduled(cron = "${schedules.cron.notification.encourage-write-review}")
    public void notifyEncourageWriteReview() {
        volunteerNotificationService.notifyEncourageWriteReview();
    }

    @Scheduled(cron = "${schedules.cron.notification.encourage-check-attendance}")
    public void notifyEncourageCheckAttendance() {
        shelterNotificationService.notifyEncourageCheckAttendance();
    }
}
