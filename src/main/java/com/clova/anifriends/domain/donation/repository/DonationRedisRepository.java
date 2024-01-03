package com.clova.anifriends.domain.donation.repository;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DonationRedisRepository implements DonationCacheRepository {

    public static final int TIMEOUT = 1;

    private final RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> valueOperations;

    @PostConstruct
    public void init() {
        valueOperations = redisTemplate.opsForValue();
    }

    public boolean isDuplicateDonation(Long volunteerId) {
        String key = "donation:" + volunteerId;
        return Boolean.FALSE.equals(
            valueOperations.setIfAbsent(key, true, TIMEOUT, TimeUnit.SECONDS));
    }
}
