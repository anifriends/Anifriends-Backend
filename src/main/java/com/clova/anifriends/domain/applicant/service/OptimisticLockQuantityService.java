package com.clova.anifriends.domain.applicant.service;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.exception.RecruitmentNotFoundException;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OptimisticLockQuantityService {

    private final RecruitmentRepository recruitmentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Recruitment increaseApplicantCount(long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
            .orElseThrow(() -> new RecruitmentNotFoundException("존재하지 않는 봉사 모집입니다."));
        if (recruitment.isFullApplicants()) {
            throw new RuntimeException("모집 정원이 초과되었습니다." + recruitment.getApplicantCount());
        }
        recruitment.increaseApplicantCount();
        return recruitment;
    }

}
