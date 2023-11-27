package com.clova.anifriends.domain.recruitment.service;

import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentCacheService {

    private final RedisTemplate<String, Long> redisTemplate;
    private final RecruitmentRepository recruitmentRepository;

    private static final Long RECRUITMENT_COUNT_NO_CACHE = -1L;
    private static final Long COUNT_ONE = 1L;

    private static final String RECRUITMENT_CACHE_KEY = "recruitment:count";

    public Long getRecruitmentCount() {
        Object cachedCount = redisTemplate.opsForValue().get(RECRUITMENT_CACHE_KEY);

        if (Objects.isNull(cachedCount)) {
            return RECRUITMENT_COUNT_NO_CACHE;
        }

        if (cachedCount instanceof Long) {
            return (Long) cachedCount;
        } else {
            return ((Integer) cachedCount).longValue();
        }
    }

    public void registerRecruitmentCount(
        Long count
    ) {
        redisTemplate.opsForValue().set(RECRUITMENT_CACHE_KEY, count);
    }

    public void plusOneToRecruitmentCount() {
        Object cachedCount = redisTemplate.opsForValue().get(RECRUITMENT_CACHE_KEY);

        if (Objects.nonNull(cachedCount)) {
            registerRecruitmentCount((Integer) cachedCount + COUNT_ONE);
        }

        long dbRecruitmentCount = recruitmentRepository.count();

        registerRecruitmentCount(dbRecruitmentCount);
    }

    public void minusOneToRecruitmentCount() {
        Object cachedCount = redisTemplate.opsForValue().get(RECRUITMENT_CACHE_KEY);

        if (Objects.nonNull(cachedCount)) {
            registerRecruitmentCount((Integer) cachedCount - COUNT_ONE);
        }

        long dbRecruitmentCount = recruitmentRepository.count();

        registerRecruitmentCount(dbRecruitmentCount);
    }
}
