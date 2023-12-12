package com.clova.anifriends.domain.recruitment.repository;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse.FindRecruitmentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface RecruitmentCacheRepository {

    void saveRecruitment(Recruitment recruitment);

    Slice<FindRecruitmentResponse> findRecruitments(Pageable pageable);

    void updateRecruitment(Recruitment recruitment);

    void deleteRecruitment(Recruitment recruitment);

    void closeRecruitmentsIfNeedToBe();

    Long getRecruitmentCount();

    void saveRecruitmentCount(Long count);

    void increaseRecruitmentCount();

    void decreaseToRecruitmentCount();
}
