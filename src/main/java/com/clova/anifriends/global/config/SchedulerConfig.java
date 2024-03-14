package com.clova.anifriends.global.config;

import com.clova.anifriends.domain.notification.service.ShelterNotificationService;
import com.clova.anifriends.domain.notification.service.VolunteerNotificationService;
import com.clova.anifriends.domain.recruitment.service.RecruitmentService;
import com.clova.anifriends.global.scheduler.NotifyScheduler;
import com.clova.anifriends.global.scheduler.ServiceScheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@ConditionalOnProperty(
    value = "scheduler.enabled", havingValue = "true", matchIfMissing = true
)
@EnableScheduling
@Configuration
public class SchedulerConfig {

    @Bean
    public NotifyScheduler notifyScheduler(
        VolunteerNotificationService volunteerNotificationService,
        ShelterNotificationService shelterNotificationService
    ) {
        return new NotifyScheduler(volunteerNotificationService, shelterNotificationService);
    }

    @Bean
    public ServiceScheduler serviceScheduler(RecruitmentService recruitmentService) {
        return new ServiceScheduler(recruitmentService);
    }
}
