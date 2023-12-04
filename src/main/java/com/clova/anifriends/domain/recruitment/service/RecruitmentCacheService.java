package com.clova.anifriends.domain.recruitment.service;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentCacheRepository;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitmentCacheService {

    private static final Long RECRUITMENT_COUNT_NO_CACHE = -1L;
    private static final int MAX_CACHED_SIZE = 30;
    private static final Long COUNT_ONE = 1L;
    private static final String RECRUITMENT_CACHE_KEY = "recruitment:count";

    private final RecruitmentRepository recruitmentRepository;
    private final RecruitmentCacheRepository recruitmentCacheRepository;
    private final RedisTemplate<String, Long> redisTemplate;


    @Transactional(readOnly = true)
    public void synchronizeCache() {
        PageRequest pageRequest = PageRequest.of(0, MAX_CACHED_SIZE);
        Slice<Recruitment> recruitmentSlice = recruitmentRepository.findRecruitmentsV2(null, null,
            null, null, true, true, true, null,
            null, pageRequest);
        List<Recruitment> findRecruitments = recruitmentSlice.getContent();
        findRecruitments.forEach(recruitmentCacheRepository::save);
    }

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
