package com.clova.anifriends.domain.recruitment.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Long RECRUITMENT_COUNT_NO_CACHE = -1L;

    public Long getRecruitmentCount(
        String cacheKey
    ) {
        Object cachedCount = redisTemplate.opsForValue().get(cacheKey);

        if (Objects.isNull(cachedCount)) {
            return RECRUITMENT_COUNT_NO_CACHE;
        }

        if (cachedCount instanceof Long) {
            return (Long) cachedCount;
        }

        return ((Integer) cachedCount).longValue();
    }

    public void registerRecruitmentCount(
        String cacheKey,
        Long dbCount
    ) {
        redisTemplate.opsForValue().set(cacheKey, dbCount);
    }
}
