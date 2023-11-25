package com.clova.anifriends.domain.recruitment.controller;

import com.clova.anifriends.domain.auth.LoginUser;
import com.clova.anifriends.domain.auth.authorization.ShelterOnly;
import com.clova.anifriends.domain.recruitment.dto.request.FindRecruitmentsByShelterRequest;
import com.clova.anifriends.domain.recruitment.dto.request.FindRecruitmentsRequest;
import com.clova.anifriends.domain.recruitment.dto.request.FindRecruitmentsRequestV2;
import com.clova.anifriends.domain.recruitment.dto.request.RegisterRecruitmentRequest;
import com.clova.anifriends.domain.recruitment.dto.request.UpdateRecruitmentRequest;
import com.clova.anifriends.domain.recruitment.dto.response.FindCompletedRecruitmentsResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentDetailResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByShelterIdResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByShelterResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse;
import com.clova.anifriends.domain.recruitment.dto.response.RegisterRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.service.RecruitmentService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @ShelterOnly
    @PostMapping("/shelters/recruitments")
    public ResponseEntity<RegisterRecruitmentResponse> registerRecruitment(
        @LoginUser Long volunteerId,
        @RequestBody @Valid RegisterRecruitmentRequest registerRecruitmentRequest) {
        RegisterRecruitmentResponse registerRecruitmentResponse
            = recruitmentService.registerRecruitment(
            volunteerId,
            registerRecruitmentRequest.title(),
            registerRecruitmentRequest.startTime(),
            registerRecruitmentRequest.endTime(),
            registerRecruitmentRequest.deadline(),
            registerRecruitmentRequest.capacity(),
            registerRecruitmentRequest.content(),
            registerRecruitmentRequest.imageUrls());
        URI location
            = URI.create("/api/recruitments/" + registerRecruitmentResponse.recruitmentId());
        return ResponseEntity.created(location).body(registerRecruitmentResponse);
    }

    @GetMapping("/recruitments/{recruitmentId}")
    public ResponseEntity<FindRecruitmentDetailResponse> findRecruitmentDetail(
        @PathVariable Long recruitmentId
    ) {
        return ResponseEntity.ok(recruitmentService.findRecruitmentDetail(recruitmentId));
    }

    @ShelterOnly
    @GetMapping("/shelters/volunteers/{volunteerId}/recruitments/completed")
    public ResponseEntity<FindCompletedRecruitmentsResponse> findCompletedRecruitments(
        @PathVariable("volunteerId") Long volunteerId,
        Pageable pageable) {
        FindCompletedRecruitmentsResponse response = recruitmentService.findCompletedRecruitments(
            volunteerId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recruitments")
    public ResponseEntity<FindRecruitmentsResponse> findRecruitments(
        @ModelAttribute @Valid FindRecruitmentsRequest findRecruitmentsRequest,
        Pageable pageable) {
        KeywordCondition keywordCondition = findRecruitmentsRequest.keywordFilter()
            .getKeywordCondition();
        return ResponseEntity.ok(recruitmentService.findRecruitments(
            findRecruitmentsRequest.keyword(),
            findRecruitmentsRequest.startDate(),
            findRecruitmentsRequest.endDate(),
            findRecruitmentsRequest.closedFilter().getIsClosed(),
            keywordCondition.titleFilter(),
            keywordCondition.contentFilter(),
            keywordCondition.shelterNameFilter(),
            pageable
        ));
    }

    @GetMapping("/v2/recruitments")
    public ResponseEntity<FindRecruitmentsResponse> findRecruitmentsV2(
        @ModelAttribute @Valid FindRecruitmentsRequestV2 findRecruitmentsRequestV2,
        Pageable pageable) {
        KeywordCondition keywordCondition = findRecruitmentsRequestV2.keywordFilter()
            .getKeywordCondition();

        return ResponseEntity.ok(recruitmentService.findRecruitmentsV2(
            findRecruitmentsRequestV2.keyword(),
            findRecruitmentsRequestV2.startDate(),
            findRecruitmentsRequestV2.endDate(),
            findRecruitmentsRequestV2.closedFilter().getIsClosed(),
            keywordCondition.titleFilter(),
            keywordCondition.contentFilter(),
            keywordCondition.shelterNameFilter(),
            findRecruitmentsRequestV2.createdAt(),
            findRecruitmentsRequestV2.recruitmentId(),
            pageable
        ));
    }

    @ShelterOnly
    @GetMapping("/shelters/recruitments")
    public ResponseEntity<FindRecruitmentsByShelterResponse> findRecruitmentsByShelter(
        @LoginUser Long shelterId,
        @ModelAttribute @Valid FindRecruitmentsByShelterRequest findRecruitmentsByShelterRequest,
        Pageable pageable
    ) {
        KeywordConditionByShelter keywordConditionByShelter = findRecruitmentsByShelterRequest.keywordFilter()
            .getKeywordConditionByShelter();

        return ResponseEntity.ok(recruitmentService.findRecruitmentsByShelter(
            shelterId,
            findRecruitmentsByShelterRequest.keyword(),
            findRecruitmentsByShelterRequest.startDate(),
            findRecruitmentsByShelterRequest.endDate(),
            findRecruitmentsByShelterRequest.closedFilter().getIsClosed(),
            keywordConditionByShelter.contentFilter(),
            keywordConditionByShelter.titleFilter(),
            pageable
        ));
    }

    @GetMapping("/shelters/{shelterId}/recruitments")
    public ResponseEntity<FindRecruitmentsByShelterIdResponse> findShelterRecruitmentsByShelter(
        @PathVariable Long shelterId,
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            recruitmentService.findShelterRecruitmentsByShelter(shelterId, pageable));
    }

    @ShelterOnly
    @PatchMapping("/shelters/recruitments/{recruitmentId}/close")
    public ResponseEntity<Void> closeRecruitment(
        @LoginUser Long shelterId,
        @PathVariable Long recruitmentId) {
        recruitmentService.closeRecruitment(shelterId, recruitmentId);
        return ResponseEntity.noContent().build();
    }

    @ShelterOnly
    @PatchMapping("/shelters/recruitments/{recruitmentId}")
    public ResponseEntity<Void> updateRecruitment(
        @LoginUser Long shelterId,
        @PathVariable Long recruitmentId,
        @RequestBody @Valid UpdateRecruitmentRequest updateRecruitmentRequest) {
        recruitmentService.updateRecruitment(
            shelterId,
            recruitmentId,
            updateRecruitmentRequest.title(),
            updateRecruitmentRequest.startTime(),
            updateRecruitmentRequest.endTime(),
            updateRecruitmentRequest.deadline(),
            updateRecruitmentRequest.capacity(),
            updateRecruitmentRequest.content(),
            updateRecruitmentRequest.imageUrls());
        return ResponseEntity.noContent().build();
    }

    @ShelterOnly
    @DeleteMapping("/shelters/recruitments/{recruitmentId}")
    public ResponseEntity<Void> deleteRecruitment(
        @LoginUser Long shelterId,
        @PathVariable Long recruitmentId) {
        recruitmentService.deleteRecruitment(shelterId, recruitmentId);
        return ResponseEntity.noContent().build();
    }
}
