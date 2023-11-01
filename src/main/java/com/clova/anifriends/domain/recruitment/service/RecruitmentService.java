package com.clova.anifriends.domain.recruitment.service;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentByShelterResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentByVolunteerResponse;
import com.clova.anifriends.domain.recruitment.exception.RecruitmentNotFoundException;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;

    public FindRecruitmentByShelterResponse findRecruitmentByIdByShelter(long id) {
        Recruitment recruitment = getRecruitmentById(id);
        return FindRecruitmentByShelterResponse.from(recruitment);
    }

    public FindRecruitmentByVolunteerResponse findRecruitmentByIdByVolunteer(long id) {
        Recruitment recruitment = getRecruitmentById(id);
        return FindRecruitmentByVolunteerResponse.from(recruitment);
    }

    private Recruitment getRecruitmentById(long id) {
        return recruitmentRepository.findById(id)
            .orElseThrow(() -> new RecruitmentNotFoundException("존재하지 않는 모집글입니다."));
    }
}
