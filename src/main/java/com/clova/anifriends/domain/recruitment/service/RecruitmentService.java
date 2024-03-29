package com.clova.anifriends.domain.recruitment.service;

import com.clova.anifriends.domain.common.event.ImageDeletionEvent;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindCompletedRecruitmentsResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentDetailResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByShelterResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse.FindRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindShelterRecruitmentsResponse;
import com.clova.anifriends.domain.recruitment.dto.response.RegisterRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.exception.RecruitmentNotFoundException;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentCacheRepository;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private static final Long RECRUITMENT_COUNT_NO_CACHE = -1L;

    private final ShelterRepository shelterRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RecruitmentCacheRepository recruitmentCacheRepository;

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
        recruitmentCacheRepository.saveRecruitment(recruitment);
        recruitmentCacheRepository.increaseRecruitmentCount();

        return RegisterRecruitmentResponse.from(recruitment);
    }

    @Transactional(readOnly = true)
    public FindRecruitmentsByShelterResponse findRecruitmentsByShelter(
        Long shelterId,
        String keyword,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isClosed,
        KeywordConditionByShelter keywordConditionByShelter,
        Pageable pageable
    ) {
        Page<Recruitment> pagination = recruitmentRepository.findRecruitmentsByShelterOrderByCreatedAt(
            shelterId,
            keyword,
            startDate,
            endDate,
            isClosed,
            keywordConditionByShelter,
            pageable
        );

        return FindRecruitmentsByShelterResponse.from(pagination);
    }

    @Transactional(readOnly = true)
    public FindShelterRecruitmentsResponse findShelterRecruitments(
        long shelterId, Pageable pageable
    ) {
        Page<Recruitment> pagination = recruitmentRepository.findShelterRecruitments(
            shelterId, pageable
        );
        return FindShelterRecruitmentsResponse.from(pagination);
    }

    @Transactional(readOnly = true)
    public FindRecruitmentDetailResponse findRecruitmentDetail(long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findRecruitmentDetail(recruitmentId)
            .orElseThrow(() -> new RecruitmentNotFoundException("존재하지 않는 모집글입니다."));
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
        KeywordCondition keywordCondition,
        Pageable pageable) {
        Page<Recruitment> recruitments = recruitmentRepository.findRecruitments(
            keyword,
            startDate,
            endDate,
            isClosed,
            keywordCondition,
            pageable);
        return FindRecruitmentsResponse.from(recruitments);
    }

    @Transactional(readOnly = true)
    public FindRecruitmentsResponse findRecruitmentsV2(
        String keyword,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isClosed,
        KeywordCondition keywordCondition,
        LocalDateTime createdAt,
        Long recruitmentId,
        Pageable pageable
    ) {
        Long count = recruitmentCacheRepository.getRecruitmentCount();
        if (findRecruitmentsWithoutCondition(keyword, startDate, endDate, isClosed,
            keywordCondition, recruitmentId)) {
            if (Objects.equals(count, RECRUITMENT_COUNT_NO_CACHE)) {
                count = recruitmentRepository.countFindRecruitmentsV2(
                    keyword,
                    startDate,
                    endDate,
                    isClosed,
                    keywordCondition
                );
                recruitmentCacheRepository.saveRecruitmentCount(count);
            }
        } else {
            count = recruitmentRepository.countFindRecruitmentsV2(
                keyword,
                startDate,
                endDate,
                isClosed,
                keywordCondition
            );
        }

        if (findRecruitmentsWithoutCondition(keyword, startDate, endDate, isClosed,
            keywordCondition, recruitmentId)) {
            Slice<FindRecruitmentResponse> cachedRecruitments
                = recruitmentCacheRepository.findRecruitments(pageable);
            if (canTrustCached(cachedRecruitments)) {
                return FindRecruitmentsResponse.fromCached(cachedRecruitments, count);
            }
        }
        Slice<Recruitment> recruitments = recruitmentRepository.findRecruitmentsV2(
            keyword,
            startDate,
            endDate,
            isClosed,
            keywordCondition,
            createdAt,
            recruitmentId,
            pageable);
        return FindRecruitmentsResponse.fromV2(recruitments, count);
    }

    private boolean canTrustCached(Slice<FindRecruitmentResponse> cachedRecruitments) {
        return cachedRecruitments.hasNext();
    }

    private boolean findRecruitmentsWithoutCondition(String keyword, LocalDate startDate,
        LocalDate endDate, Boolean isClosed, KeywordCondition keywordCondition, Long recruitmentId) {
        return Objects.isNull(keyword) && Objects.isNull(keywordCondition)
            && Objects.isNull(startDate) && Objects.isNull(endDate) && Objects.isNull(isClosed)
            && Objects.isNull(recruitmentId);
    }

    @Transactional
    public void closeRecruitment(Long shelterId, Long recruitmentId) {
        Recruitment recruitment = getRecruitmentByShelter(shelterId, recruitmentId);
        recruitmentCacheRepository.updateRecruitment(recruitment);
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

        List<String> imagesToDelete = recruitment.findImagesToDelete(imageUrls);
        applicationEventPublisher.publishEvent(new ImageDeletionEvent(imagesToDelete));

        recruitment.updateRecruitment(
            title,
            startTime,
            endTime,
            deadline,
            capacity,
            content,
            imageUrls
        );
        recruitmentCacheRepository.updateRecruitment(recruitment);
    }

    @Transactional
    public void deleteRecruitment(Long shelterId, Long recruitmentId) {
        Recruitment recruitment = getRecruitmentByShelter(shelterId, recruitmentId);
        recruitment.checkDeletable();

        List<String> imagesToDelete = recruitment.getImages();
        applicationEventPublisher.publishEvent(new ImageDeletionEvent(imagesToDelete));

        recruitmentRepository.delete(recruitment);
        recruitmentCacheRepository.deleteRecruitment(recruitment);
        recruitmentCacheRepository.decreaseToRecruitmentCount();
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

    @Transactional
    public void autoCloseRecruitment() {
        recruitmentRepository.closeRecruitmentsIfNeedToBe();
        recruitmentCacheRepository.closeRecruitmentsIfNeedToBe();
    }
}
