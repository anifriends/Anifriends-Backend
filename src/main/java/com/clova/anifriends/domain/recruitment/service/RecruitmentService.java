package com.clova.anifriends.domain.recruitment.service;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.request.RegisterRecruitmentRequest;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentByShelterResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentByVolunteerResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindShelterSimpleResponse;
import com.clova.anifriends.domain.recruitment.dto.response.RegisterRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.exception.RecruitmentNotFoundException;
import com.clova.anifriends.domain.recruitment.mapper.RecruitmentMapper;
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
        Shelter shelter = getShelterById(shelterId);
        Recruitment recruitment = RecruitmentMapper.toRecruitment(shelter, request);
        recruitmentRepository.save(recruitment);
        return RegisterRecruitmentResponse.from(recruitment);
    }

    private Shelter getShelterById(Long shelterId) {
        return shelterRepository.findById(shelterId)
            .orElseThrow(() -> new ShelterNotFoundException("존재하지 않는 보호소입니다."));
    }

    public FindRecruitmentByShelterResponse findRecruitmentByIdByShelter(long id) {
        Recruitment recruitment = getRecruitmentById(id);
        return FindRecruitmentByShelterResponse.from(recruitment);
    }

    public FindRecruitmentByVolunteerResponse findRecruitmentByIdByVolunteer(long id) {
        Recruitment recruitment = getRecruitmentById(id);
        return FindRecruitmentByVolunteerResponse.from(recruitment);
    }

    @Transactional(readOnly = true)
    public FindShelterSimpleResponse findShelterSimple(
        Long recruitmentId
    ) {
        Recruitment foundRecruitment = getRecruitmentById(recruitmentId);

        return FindShelterSimpleResponse.from(foundRecruitment);
    }

    private Recruitment getRecruitmentById(long id) {
        return recruitmentRepository.findById(id)
            .orElseThrow(() -> new RecruitmentNotFoundException("존재하지 않는 모집글입니다."));
    }
}
