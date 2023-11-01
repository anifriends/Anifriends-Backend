package com.clova.anifriends.domain.recruitment.service;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.RegisterRecruitmentRequest;
import com.clova.anifriends.domain.recruitment.dto.RegisterRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentByShelterResponse;
import com.clova.anifriends.domain.recruitment.exception.RecruitmentNotFoundException;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final ShelterRepository shelterRepository;
    private final RecruitmentRepository recruitmentRepository;

    @Transactional
    public RegisterRecruitmentResponse registerRecruitment(
        Long shelterId,
        RegisterRecruitmentRequest request) {
        Shelter shelter = shelterRepository.findById(shelterId)
            .orElseThrow(() -> new ShelterNotFoundException("존재하지 않는 보호소입니다."));
        Recruitment recruitment = new Recruitment(
            shelter,
            request.title(),
            request.capacity(),
            request.content(),
            request.startTime(),
            request.endTime(),
            request.deadline(),
            request.imageUrls());
        recruitmentRepository.save(recruitment);
        return RegisterRecruitmentResponse.from(recruitment);
    }

    public FindRecruitmentByShelterResponse findRecruitmentByIdByShelter(long id) {
        Recruitment recruitment = getRecruitmentById(id);
        return FindRecruitmentByShelterResponse.from(recruitment);
    }

    private Recruitment getRecruitmentById(long id) {
        return recruitmentRepository.findById(id)
            .orElseThrow(() -> new RecruitmentNotFoundException("존재하지 않는 모집글입니다."));
    }
}
