package com.clova.anifriends.global.scheduler;

import com.clova.anifriends.domain.recruitment.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceScheduler {

    private final RecruitmentService recruitmentService;

    @Scheduled(cron = "${schedules.cron.recruitment.auto-close}")
    public void autoCloseRecruitment() {
        recruitmentService.autoCloseRecruitment();
    }
}
