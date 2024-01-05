package com.clova.anifriends.domain.donation.repository;

import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class DonationRedisRepository implements DonationCacheRepository {

    public static final int TIMEOUT = 1;
    public static final String DONATION_KEY = "donation:";

    private final ValueOperations<String, Object> valueOperations;

    public DonationRedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
    }

    public boolean isDuplicateDonation(Long volunteerId) {
        String key = DONATION_KEY + volunteerId;
        return Boolean.FALSE.equals(
            valueOperations.setIfAbsent(key, true, TIMEOUT, TimeUnit.SECONDS));
    }
}
