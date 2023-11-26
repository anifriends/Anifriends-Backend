package com.clova.anifriends.domain.recruitment.repository;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse.FindRecruitmentResponse;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecruitmentCacheRepository {

    private static final String RECRUITMENT_KEY = "recruitment";
    private static final int UNTIL_LAST_ELEMENT = -1;
    private static final int CACHED_SIZE = 20;
    private static final int ZERO = 0;

    private final RedisTemplate<String, FindRecruitmentResponse> redisTemplate;

    public void save(final Recruitment recruitment) {
        FindRecruitmentResponse recruitmentResponse = FindRecruitmentResponse.from(recruitment);
        ZSetOperations<String, FindRecruitmentResponse> cachedRecruitments = redisTemplate.opsForZSet();
        long createdAtScore = getCreatedAtScore(recruitment);
        cachedRecruitments.add(RECRUITMENT_KEY, recruitmentResponse, createdAtScore);

        popUntilCachedSize(cachedRecruitments);
    }

    private void popUntilCachedSize(
        ZSetOperations<String, FindRecruitmentResponse> cachedRecruitments) {
        Set<FindRecruitmentResponse> recruitments
            = cachedRecruitments.range(RECRUITMENT_KEY, ZERO, UNTIL_LAST_ELEMENT);
        if (Objects.nonNull(recruitments)) {
            int needToRemoveSize = recruitments.size() - CACHED_SIZE;
            needToRemoveSize = Math.max(needToRemoveSize, ZERO);
            cachedRecruitments.popMin(RECRUITMENT_KEY, needToRemoveSize);
        }
    }

    public List<FindRecruitmentResponse> findAll() {
        ZSetOperations<String, FindRecruitmentResponse> cachedRecruitments
            = redisTemplate.opsForZSet();
        Set<FindRecruitmentResponse> recruitments
            = cachedRecruitments.reverseRange(RECRUITMENT_KEY, ZERO, CACHED_SIZE);
        if (Objects.isNull(recruitments)) {
            return List.of();
        }
        return recruitments.stream()
            .toList();
    }

    public void update(final Recruitment recruitment) {
        long createdAtScore = getCreatedAtScore(recruitment);
        ZSetOperations<String, FindRecruitmentResponse> cachedRecruitments
            = redisTemplate.opsForZSet();
        Set<FindRecruitmentResponse> recruitments = cachedRecruitments.rangeByScore(
            RECRUITMENT_KEY, createdAtScore, createdAtScore);
        if (Objects.nonNull(recruitments)) {
            Optional<FindRecruitmentResponse> oldCachedRecruitment = recruitments.stream()
                .filter(findRecruitmentResponse -> isEqualsId(recruitment, findRecruitmentResponse))
                .findFirst();
            oldCachedRecruitment.ifPresent(oldRecruitment -> {
                cachedRecruitments.remove(RECRUITMENT_KEY, oldRecruitment);
                FindRecruitmentResponse updatedRecruitment
                    = FindRecruitmentResponse.from(recruitment);
                cachedRecruitments.add(RECRUITMENT_KEY, updatedRecruitment, createdAtScore);
            });
        }
    }

    private long getCreatedAtScore(Recruitment recruitment) {
        return recruitment.getCreatedAt().toEpochSecond(ZoneOffset.UTC);
    }

    private boolean isEqualsId(
        Recruitment recruitment,
        FindRecruitmentResponse findRecruitmentResponse) {
        return findRecruitmentResponse.recruitmentId().equals(recruitment.getRecruitmentId());
    }

    public void delete(final Recruitment recruitment) {
        ZSetOperations<String, FindRecruitmentResponse> cachedRecruitments
            = redisTemplate.opsForZSet();
        FindRecruitmentResponse recruitmentResponse = FindRecruitmentResponse.from(recruitment);
        cachedRecruitments.remove(RECRUITMENT_KEY, recruitmentResponse);
    }
}
