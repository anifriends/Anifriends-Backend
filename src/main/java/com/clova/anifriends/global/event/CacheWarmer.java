package com.clova.anifriends.global.event;

import com.clova.anifriends.domain.recruitment.service.RecruitmentCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheWarmer {

    private final RecruitmentCacheService recruitmentCacheService;

    @EventListener(ApplicationReadyEvent.class)
    void synchronizeRecruitmentCache() {
        recruitmentCacheService.synchronizeCache();
    }
}
