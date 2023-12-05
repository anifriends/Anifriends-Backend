package com.clova.anifriends.domain.recruitment.repository;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse.FindRecruitmentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface RecruitmentCacheRepository {

    void save(Recruitment recruitment);

    Slice<FindRecruitmentResponse> findAll(Pageable pageable);

    void update(Recruitment recruitment);

    void delete(Recruitment recruitment);

    void closeRecruitmentsIfNeedToBe();
}
