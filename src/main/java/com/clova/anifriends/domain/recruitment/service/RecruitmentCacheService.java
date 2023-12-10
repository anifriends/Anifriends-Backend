package com.clova.anifriends.domain.recruitment.service;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRedisRepository;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitmentCacheService {

    private static final int MAX_CACHED_SIZE = 30;

    private final RecruitmentRepository recruitmentRepository;
    private final RecruitmentRedisRepository recruitmentRedisRepository;

    @Transactional(readOnly = true)
    public void synchronizeRecruitmentsCache() {
        PageRequest pageRequest = PageRequest.of(0, MAX_CACHED_SIZE);
        Slice<Recruitment> recruitmentSlice = recruitmentRepository.findRecruitmentsV2(null, null,
            null, null, true, true, true, null,
            null, pageRequest);
        List<Recruitment> findRecruitments = recruitmentSlice.getContent();
        findRecruitments.forEach(recruitmentRedisRepository::saveRecruitment);
    }
}
