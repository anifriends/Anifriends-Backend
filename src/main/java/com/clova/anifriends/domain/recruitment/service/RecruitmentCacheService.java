package com.clova.anifriends.domain.recruitment.service;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse.FindRecruitmentResponse;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentCacheService {

    private static final String RECRUITMENT_KEY = "recruitment";
    private static final int UNTIL_LAST_ELEMENT = -1;
    private static final int CACHED_SIZE = 20;
    private static final int ZERO = 0;

    private final RedisTemplate<String, FindRecruitmentResponse> redisTemplate;

    public void pushNewRecruitment(final Recruitment recruitment) {
        FindRecruitmentResponse recruitmentResponse = FindRecruitmentResponse.from(recruitment);
        ZSetOperations<String, FindRecruitmentResponse> cachedRecruitments = redisTemplate.opsForZSet();
        long createdAtScore = recruitment.getCreatedAt().toEpochSecond(ZoneOffset.UTC);
        cachedRecruitments.add(RECRUITMENT_KEY, recruitmentResponse, createdAtScore);

        popUntilCachedSize(cachedRecruitments);
    }

    private void popUntilCachedSize(ZSetOperations<String, FindRecruitmentResponse> cachedRecruitments) {
        Set<FindRecruitmentResponse> recruitments
            = cachedRecruitments.range(RECRUITMENT_KEY, ZERO, UNTIL_LAST_ELEMENT);
        if(Objects.nonNull(recruitments)) {
            int needToRemoveSize = recruitments.size() - CACHED_SIZE;
            needToRemoveSize = Math.max(needToRemoveSize, ZERO);
            cachedRecruitments.popMin(RECRUITMENT_KEY, needToRemoveSize);
        }
    }

    public List<FindRecruitmentResponse> findRecruitments() {
        ZSetOperations<String, FindRecruitmentResponse> stringObjectZSetOperations = redisTemplate.opsForZSet();
        Set<FindRecruitmentResponse> set = stringObjectZSetOperations.reverseRange(RECRUITMENT_KEY, ZERO,
            CACHED_SIZE);
        if(Objects.nonNull(set)) {
            return new ArrayList<>(set);
        }
        return List.of();
    }
}
