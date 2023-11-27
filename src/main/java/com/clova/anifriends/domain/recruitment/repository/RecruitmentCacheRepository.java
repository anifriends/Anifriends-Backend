package com.clova.anifriends.domain.recruitment.repository;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse.FindRecruitmentResponse;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RecruitmentCacheRepository {

    private static final String RECRUITMENT_KEY = "recruitment";
    private static final int UNTIL_LAST_ELEMENT = -1;
    private static final int MAX_CACHED_SIZE = 30;
    private static final int PAGE_SIZE = 20;
    private static final int ZERO = 0;

    private final ZSetOperations<String, FindRecruitmentResponse> cachedRecruitments;

    public RecruitmentCacheRepository(RedisTemplate<String, FindRecruitmentResponse> redisTemplate) {
        this.cachedRecruitments = redisTemplate.opsForZSet();
    }

    public void save(final Recruitment recruitment) {
        FindRecruitmentResponse recruitmentResponse = FindRecruitmentResponse.from(recruitment);
        long createdAtScore = getCreatedAtScore(recruitment);
        cachedRecruitments.add(RECRUITMENT_KEY, recruitmentResponse, createdAtScore);

        popUntilCachedSize();
    }

    private void popUntilCachedSize() {
        Set<FindRecruitmentResponse> recruitments
            = cachedRecruitments.range(RECRUITMENT_KEY, ZERO, UNTIL_LAST_ELEMENT);
        if (Objects.nonNull(recruitments)) {
            int needToRemoveSize = recruitments.size() - MAX_CACHED_SIZE;
            needToRemoveSize = Math.max(needToRemoveSize, ZERO);
            cachedRecruitments.popMin(RECRUITMENT_KEY, needToRemoveSize);
        }
    }

    /**
     * 캐시된 Recruitment dto 리스트를 size만큼 조회합니다. size가 지정된 최대 size를 초과하는 경우 지정된 최대 size만큼 조회해옵니다.
     *
     * @param pageable 조회해 올 리스트 pageable. 최대 사이즈 20
     * @return 캐시된 Recruitment dto 리스트
     */
    public Slice<FindRecruitmentResponse> findAll(Pageable pageable) {
        long size = pageable.getPageSize();
        if (size > PAGE_SIZE) {
            size = PAGE_SIZE;
        }
        Set<FindRecruitmentResponse> recruitments
            = cachedRecruitments.reverseRange(RECRUITMENT_KEY, ZERO, size);
        if (Objects.isNull(recruitments)) {
            return new SliceImpl<>(List.of());
        }
        List<FindRecruitmentResponse> content = recruitments.stream()
            .limit(size)
            .toList();
        boolean hasNext = recruitments.size() > size;
        return new SliceImpl<>(content, pageable, hasNext);
    }

    public void update(final Recruitment recruitment) {
        long createdAtScore = getCreatedAtScore(recruitment);
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
        FindRecruitmentResponse recruitmentResponse = FindRecruitmentResponse.from(recruitment);
        cachedRecruitments.remove(RECRUITMENT_KEY, recruitmentResponse);
    }
}
