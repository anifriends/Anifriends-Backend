package com.clova.anifriends.domain.recruitment.service;

import com.clova.anifriends.domain.common.dto.PageInfo;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.request.RegisterRecruitmentRequest;
import com.clova.anifriends.domain.recruitment.dto.response.FindCompletedRecruitmentsResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentByShelterResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentDetailByVolunteerResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByShelterResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByVolunteerResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindShelterSimpleResponse;
import com.clova.anifriends.domain.recruitment.dto.response.RegisterRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.exception.RecruitmentNotFoundException;
import com.clova.anifriends.domain.recruitment.mapper.RecruitmentMapper;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public FindRecruitmentsByShelterResponse findRecruitmentsByShelter(
        Long shelterId,
        String keyword,
        LocalDate startDate,
        LocalDate endDate,
        Boolean content,
        Boolean title,
        Pageable pageable
    ) {
        Page<Recruitment> pagination = recruitmentRepository.findRecruitmentsByShelterOrderByCreatedAt(
            shelterId,
            keyword,
            startDate,
            endDate,
            content,
            title,
            pageable
        );

        return FindRecruitmentsByShelterResponse.of(pagination.getContent(),
            PageInfo.from(pagination));
    }

    private Shelter getShelterById(Long shelterId) {
        return shelterRepository.findById(shelterId)
            .orElseThrow(() -> new ShelterNotFoundException("존재하지 않는 보호소입니다."));
    }

    public FindRecruitmentByShelterResponse findRecruitmentByShelterIdAndRecruitmentIdByShelter(
        long shelterId, long recruitmentId) {
        Recruitment recruitment = getRecruitmentByShelterIdAndRecruitmentId(shelterId,
            recruitmentId);
        return FindRecruitmentByShelterResponse.from(recruitment);
    }

    public FindRecruitmentDetailByVolunteerResponse findRecruitmentByIdByVolunteer(long id) {
        Recruitment recruitment = getRecruitmentById(id);
        return FindRecruitmentDetailByVolunteerResponse.from(recruitment);
    }

    @Transactional(readOnly = true)
    public FindShelterSimpleResponse findShelterSimple(
        Long recruitmentId
    ) {
        Recruitment foundRecruitment = getRecruitmentById(recruitmentId);

        return FindShelterSimpleResponse.from(foundRecruitment);
    }

    private Recruitment getRecruitmentByShelterIdAndRecruitmentId(long shelterId,
        long recruitmentId) {
        return recruitmentRepository.findByShelterIdAndRecruitmentId(shelterId, recruitmentId)
            .orElseThrow(() -> new RecruitmentNotFoundException("존재하지 않는 모집글입니다."));
    }

    private Recruitment getRecruitmentById(long id) {
        return recruitmentRepository.findById(id)
            .orElseThrow(() -> new RecruitmentNotFoundException("존재하지 않는 모집글입니다."));
    }

    @Transactional(readOnly = true)
    public FindCompletedRecruitmentsResponse findCompletedRecruitments(
        Long volunteerId,
        Pageable pageable) {
        Page<Recruitment> recruitmentPage
            = recruitmentRepository.findCompletedRecruitments(volunteerId, pageable);
        return FindCompletedRecruitmentsResponse.from(recruitmentPage);
    }

    @Transactional(readOnly = true)
    public FindRecruitmentsByVolunteerResponse findRecruitmentsByVolunteer(
        String keyword, LocalDate startDate, LocalDate endDate, Boolean isClosed,
        Boolean titleContains,
        Boolean contentContains, Boolean shelterNameContains, Pageable pageable) {
        Page<Recruitment> recruitments = recruitmentRepository.findRecruitments(
            keyword,
            startDate,
            endDate,
            isClosed,
            titleContains,
            contentContains,
            shelterNameContains,
            pageable);
        return FindRecruitmentsByVolunteerResponse.from(recruitments);
    }
}
