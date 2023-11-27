package com.clova.anifriends.global.event;

import com.clova.anifriends.domain.animal.repository.AnimalCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheWarmer {

    private final AnimalCacheRepository animalCacheRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        animalCacheRepository.synchronizeCache();
    }
}
