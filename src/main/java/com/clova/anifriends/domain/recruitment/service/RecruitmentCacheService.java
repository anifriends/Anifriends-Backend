package com.clova.anifriends.domain.recruitment.service;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.controller.KeywordCondition;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentCacheRepository;
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
    private static final KeywordCondition ALL_CONTAINS_CONDITION
        = new KeywordCondition(true, true, true);

    private final RecruitmentRepository recruitmentRepository;
    private final RecruitmentCacheRepository recruitmentCacheRepository;

    @Transactional(readOnly = true)
    public void synchronizeRecruitmentsCache() {
        PageRequest pageRequest = PageRequest.of(0, MAX_CACHED_SIZE);
        Slice<Recruitment> recruitmentSlice = recruitmentRepository.findRecruitmentsV2(null, null,
            null, null, ALL_CONTAINS_CONDITION, null, null, pageRequest);
        List<Recruitment> findRecruitments = recruitmentSlice.getContent();
        findRecruitments.forEach(recruitmentCacheRepository::saveRecruitment);
    }
}
