package com.clova.anifriends.domain.recruitment.service;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindCompletedRecruitmentsResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentDetailResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByShelterIdResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByShelterResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse;
import com.clova.anifriends.domain.recruitment.dto.response.RegisterRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.exception.RecruitmentNotFoundException;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.global.image.S3Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
    private final S3Service s3Service;

    @Transactional
    public RegisterRecruitmentResponse registerRecruitment(
        Long shelterId,
        String title,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime deadline,
        int capacity,
        String content,
        List<String> imageUrls) {
        Shelter shelter = getShelterById(shelterId);
        Recruitment recruitment = new Recruitment(
            shelter,
            title,
            capacity,
            content,
            startTime,
            endTime,
            deadline,
            imageUrls);
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

        return FindRecruitmentsByShelterResponse.from(pagination);
    }

    @Transactional(readOnly = true)
    public FindRecruitmentsByShelterIdResponse findShelterRecruitmentsByShelter(
        long shelterId, Pageable pageable
    ) {
        Page<Recruitment> pagination = recruitmentRepository.findRecruitmentsByShelterId(
            shelterId, pageable
        );
        return FindRecruitmentsByShelterIdResponse.from(pagination);
    }

    @Transactional(readOnly = true)
    public FindRecruitmentDetailResponse findRecruitmentDetail(long recruitmentId) {
        Recruitment recruitment = getRecruitmentById(recruitmentId);
        return FindRecruitmentDetailResponse.from(recruitment);
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
    public FindRecruitmentsResponse findRecruitments(
        String keyword,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isClosed,
        Boolean titleContains,
        Boolean contentContains,
        Boolean shelterNameContains,
        Pageable pageable) {
        Page<Recruitment> recruitments = recruitmentRepository.findRecruitments(
            keyword,
            startDate,
            endDate,
            isClosed,
            titleContains,
            contentContains,
            shelterNameContains,
            pageable);
        return FindRecruitmentsResponse.from(recruitments);
    }

    @Transactional
    public void closeRecruitment(Long shelterId, Long recruitmentId) {
        Recruitment recruitment = getRecruitmentByShelter(shelterId, recruitmentId);
        recruitment.closeRecruitment();
    }

    @Transactional
    public void updateRecruitment(
        Long shelterId,
        Long recruitmentId,
        String title,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime deadline,
        Integer capacity,
        String content,
        List<String> imageUrls
    ) {
        Recruitment recruitment = getRecruitmentByShelterWithImages(shelterId, recruitmentId);
        deleteImagesFromS3(recruitment, imageUrls);
        recruitment.updateRecruitment(
            title,
            startTime,
            endTime,
            deadline,
            capacity,
            content,
            imageUrls
        );
    }

    @Transactional
    public void deleteRecruitment(Long shelterId, Long recruitmentId) {
        Recruitment recruitment = getRecruitmentByShelter(shelterId, recruitmentId);
        recruitment.checkDeletable();
        s3Service.deleteImages(recruitment.getImages());
        recruitmentRepository.delete(recruitment);
    }

    private void deleteImagesFromS3(Recruitment recruitment, List<String> imageUrls) {
        s3Service.deleteImages(
            recruitment.findDeleteImages(imageUrls == null ? List.of() : imageUrls));
    }

    private Recruitment getRecruitmentByShelterWithImages(Long shelterId, Long recruitmentId) {
        return recruitmentRepository.findByShelterIdAndRecruitmentIdWithImages(
                shelterId,
                recruitmentId)
            .orElseThrow(() -> new RecruitmentNotFoundException("존재하지 않는 봉사 모집글입니다."));
    }

    private Shelter getShelterById(Long shelterId) {
        return shelterRepository.findById(shelterId)
            .orElseThrow(() -> new ShelterNotFoundException("존재하지 않는 보호소입니다."));
    }

    private Recruitment getRecruitmentByShelter(long shelterId,
        long recruitmentId) {
        return recruitmentRepository.findByShelterIdAndRecruitmentId(shelterId, recruitmentId)
            .orElseThrow(() -> new RecruitmentNotFoundException("존재하지 않는 모집글입니다."));
    }

    private Recruitment getRecruitmentById(long id) {
        return recruitmentRepository.findById(id)
            .orElseThrow(() -> new RecruitmentNotFoundException("존재하지 않는 모집글입니다."));
    }
}
