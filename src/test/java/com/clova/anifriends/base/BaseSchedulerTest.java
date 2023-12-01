package com.clova.anifriends.base;

import com.clova.anifriends.domain.notification.service.ShelterNotificationService;
import com.clova.anifriends.domain.notification.service.VolunteerNotificationService;
import com.clova.anifriends.domain.recruitment.service.RecruitmentService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
public abstract class BaseSchedulerTest {

    @SpyBean
    protected RecruitmentService recruitmentService;

    @SpyBean
    protected VolunteerNotificationService volunteerNotificationService;

    @SpyBean
    protected ShelterNotificationService shelterNotificationService;
}
