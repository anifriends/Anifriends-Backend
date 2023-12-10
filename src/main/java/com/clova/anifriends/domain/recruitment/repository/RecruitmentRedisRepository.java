package com.clova.anifriends.domain.recruitment.repository;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse.FindRecruitmentResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RecruitmentRedisRepository implements RecruitmentCacheRepository {

    private static final String RECRUITMENT_KEY = "recruitment";
    private static final String RECRUITMENT_COUNT_KEY = "recruitment:count";
    private static final int UNTIL_LAST_ELEMENT = -1;
    private static final Long RECRUITMENT_COUNT_NO_CACHE = -1L;
    private static final Long COUNT_ONE = 1L;
    private static final int MAX_CACHED_SIZE = 30;
    private static final int PAGE_SIZE = 20;
    private static final int ZERO = 0;
    public static final ZoneOffset CREATED_AT_SCORE_TIME_ZONE = ZoneOffset.UTC;

    private final ZSetOperations<String, FindRecruitmentResponse> cachedRecruitments;
    private final ValueOperations<String, Long> cachedRecruitmentsCount;
    private final RecruitmentRepository recruitmentRepository;

    public RecruitmentRedisRepository(
        RedisTemplate<String, FindRecruitmentResponse> findRecruitmentTemplate,
        RedisTemplate<String, Long> countTemplate, RecruitmentRepository recruitmentRepository) {
        this.cachedRecruitments = findRecruitmentTemplate.opsForZSet();
        this.cachedRecruitmentsCount = countTemplate.opsForValue();
        this.recruitmentRepository = recruitmentRepository;
    }

    @Override
    public void saveRecruitment(final Recruitment recruitment) {
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
    @Override
    public Slice<FindRecruitmentResponse> findRecruitments(Pageable pageable) {
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

    @Override
    public void updateRecruitment(final Recruitment recruitment) {
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
        return recruitment.getCreatedAt().toEpochSecond(CREATED_AT_SCORE_TIME_ZONE);
    }

    private boolean isEqualsId(
        Recruitment recruitment,
        FindRecruitmentResponse findRecruitmentResponse) {
        return findRecruitmentResponse.recruitmentId().equals(recruitment.getRecruitmentId());
    }

    @Override
    public void deleteRecruitment(final Recruitment recruitment) {
        FindRecruitmentResponse recruitmentResponse = FindRecruitmentResponse.from(recruitment);
        cachedRecruitments.remove(RECRUITMENT_KEY, recruitmentResponse);
    }

    @Override
    public void closeRecruitmentsIfNeedToBe() {
        LocalDateTime now = LocalDateTime.now();
        Set<FindRecruitmentResponse> findRecruitments = cachedRecruitments.range(RECRUITMENT_KEY,
            ZERO, UNTIL_LAST_ELEMENT);
        if(Objects.nonNull(findRecruitments)) {
            Map<FindRecruitmentResponse, FindRecruitmentResponse> cachedKeyAndUpdatedValue
                = new HashMap<>();
            findRecruitments.stream()
                .filter(recruitment -> needToClose(recruitment, now))
                .forEach(recruitment -> {
                    FindRecruitmentResponse closedRecruitment = closeCachedRecruitment(recruitment);
                    cachedKeyAndUpdatedValue.put(recruitment, closedRecruitment);
                });

            cachedKeyAndUpdatedValue.forEach((key, value) -> {
                cachedRecruitments.remove(RECRUITMENT_KEY, key);
                long createdAtScore = value.recruitmentCreatedAt()
                    .toEpochSecond(CREATED_AT_SCORE_TIME_ZONE);
                cachedRecruitments.add(RECRUITMENT_KEY, value, createdAtScore);
            });
        }
    }

    private boolean needToClose(FindRecruitmentResponse recruitment, LocalDateTime now) {
        boolean isClosed = recruitment.recruitmentIsClosed();
        LocalDateTime deadline = recruitment.recruitmentDeadline();
        boolean notYetClosed = !isClosed;
        boolean passedDeadline = deadline.isBefore(now) || deadline.isEqual(now);
        return notYetClosed && passedDeadline;
    }

    private FindRecruitmentResponse closeCachedRecruitment(FindRecruitmentResponse recruitment) {
        return new FindRecruitmentResponse(
            recruitment.recruitmentId(),
            recruitment.recruitmentTitle(),
            recruitment.recruitmentStartTime(),
            recruitment.recruitmentEndTime(),
            recruitment.recruitmentDeadline(),
            true,
            recruitment.recruitmentApplicantCount(),
            recruitment.recruitmentCapacity(),
            recruitment.shelterName(),
            recruitment.shelterImageUrl(),
            recruitment.recruitmentCreatedAt()
        );
    }

    @Override
    public Long getRecruitmentCount() {
        Object cachedCount = cachedRecruitmentsCount.get(RECRUITMENT_COUNT_KEY);

        if (Objects.isNull(cachedCount)) {
            return RECRUITMENT_COUNT_NO_CACHE;
        }

        if (cachedCount instanceof Long) {
            return (Long) cachedCount;
        } else {
            return ((Integer) cachedCount).longValue();
        }
    }

    @Override
    public void saveRecruitmentCount(
        Long count
    ) {
        cachedRecruitmentsCount.set(RECRUITMENT_COUNT_KEY, count);
    }

    @Override
    public void increaseRecruitmentCount() {
        Object cachedCount = cachedRecruitmentsCount.get(RECRUITMENT_COUNT_KEY);

        if (Objects.nonNull(cachedCount)) {
            saveRecruitmentCount((Integer) cachedCount + COUNT_ONE);
        }

        long dbRecruitmentCount = recruitmentRepository.count();

        saveRecruitmentCount(dbRecruitmentCount);
    }

    @Override
    public void decreaseToRecruitmentCount() {
        Object cachedCount = cachedRecruitmentsCount.get(RECRUITMENT_COUNT_KEY);

        if (Objects.nonNull(cachedCount)) {
            saveRecruitmentCount((Integer) cachedCount - COUNT_ONE);
        }

        long dbRecruitmentCount = recruitmentRepository.count();

        saveRecruitmentCount(dbRecruitmentCount);
    }
}
