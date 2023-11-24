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

    public static final String RECRUITMENT_KEY = "recruitment";
    private final RedisTemplate<String, FindRecruitmentResponse> redisTemplate;

    public void registerRecruitment(final Recruitment recruitment) {
        FindRecruitmentResponse recruitmentResponse = FindRecruitmentResponse.from(recruitment);
        ZSetOperations<String, FindRecruitmentResponse> stringObjectZSetOperations = redisTemplate.opsForZSet();
        long epochSecond = recruitment.getCreatedAt().toEpochSecond(
            ZoneOffset.of("+09:00"));
        System.out.println(epochSecond);
        stringObjectZSetOperations.add(RECRUITMENT_KEY, recruitmentResponse, epochSecond);
        Set<FindRecruitmentResponse> set = stringObjectZSetOperations.range(RECRUITMENT_KEY,
            1, -1);
        if (Objects.nonNull(set) && set.size() > 20) {
            stringObjectZSetOperations.popMin(RECRUITMENT_KEY);
        }
    }

    public List<FindRecruitmentResponse> findRecruitments() {
        ZSetOperations<String, FindRecruitmentResponse> stringObjectZSetOperations = redisTemplate.opsForZSet();
        Set<FindRecruitmentResponse> set = stringObjectZSetOperations.reverseRange(RECRUITMENT_KEY, 1,
            20);
        if(Objects.nonNull(set)) {
            return new ArrayList<>(set);
        }
        return List.of();
    }
}
