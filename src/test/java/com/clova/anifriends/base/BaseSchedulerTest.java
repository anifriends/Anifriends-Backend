package com.clova.anifriends.base;

import com.clova.anifriends.base.BaseSchedulerTest.SchedulerConfig;
import com.clova.anifriends.domain.notification.service.ShelterNotificationService;
import com.clova.anifriends.domain.notification.service.VolunteerNotificationService;
import com.clova.anifriends.domain.recruitment.service.RecruitmentService;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(SchedulerConfig.class)
public abstract class BaseSchedulerTest {

    @TestConfiguration
    @EnableScheduling
    @ComponentScan("com.clova.anifriends.global.scheduler")
    static class SchedulerConfig {

    }

    @BeforeAll
    static void beforeAll() {
        Properties properties = System.getProperties();
        properties.setProperty("schedules.cron.notification.a-day-before-volunteer", "* * * * * ?");
        properties.setProperty("schedules.cron.notification.three-day-before-volunteer", "* * * * * ?");
        properties.setProperty("schedules.cron.notification.encourage-write-review", "* * * * * ?");
        properties.setProperty("schedules.cron.notification.encourage-check-attendance", "* * * * * ?");
        properties.setProperty("schedules.cron.recruitment.auto-close", "* * * * * ?");
    }

    @MockBean
    protected RecruitmentService recruitmentService;

    @MockBean
    protected VolunteerNotificationService volunteerNotificationService;

    @MockBean
    protected ShelterNotificationService shelterNotificationService;
}
