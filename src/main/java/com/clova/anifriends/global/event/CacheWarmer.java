package com.clova.anifriends.global.event;

import com.clova.anifriends.domain.animal.service.AnimalCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheWarmer {

    private final AnimalCacheService animalCacheService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        animalCacheService.synchronizeCache();
    }
}
