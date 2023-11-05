package com.clova.anifriends.domain.recruitment.controller;

import com.clova.anifriends.domain.auth.resolver.LoginUser;
import com.clova.anifriends.domain.recruitment.dto.request.FindRecruitmentsByShelterRequest;
import com.clova.anifriends.domain.recruitment.dto.request.FindRecruitmentsByVolunteerRequest;
import com.clova.anifriends.domain.recruitment.dto.request.RegisterRecruitmentRequest;
import com.clova.anifriends.domain.recruitment.dto.response.FindCompletedRecruitmentsResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentByShelterResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentDetailByVolunteerResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByShelterIdResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByShelterResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByVolunteerResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindShelterSimpleResponse;
import com.clova.anifriends.domain.recruitment.dto.response.RegisterRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.service.RecruitmentService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @PostMapping("/shelters/recruitments")
    public ResponseEntity<RegisterRecruitmentResponse> registerRecruitment(
        @LoginUser Long userId,
        @RequestBody @Valid RegisterRecruitmentRequest request) {
        RegisterRecruitmentResponse response = recruitmentService.registerRecruitment(
            userId,
            request.title(),
            request.startTime(),
            request.endTime(),
            request.deadline(),
            request.capacity(),
            request.content(),
            request.imageUrls());
        URI location = URI.create("/api/shelters/recruitments/" + response.recruitmentId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/shelters/recruitments/{recruitmentId}")
    public ResponseEntity<FindRecruitmentByShelterResponse> findRecruitmentByIdByShelter(
        @PathVariable Long recruitmentId) {
        return ResponseEntity.ok(recruitmentService.findRecruitmentByIdByShelter(recruitmentId));
    }

    @GetMapping("/volunteers/recruitments/{recruitmentId}")
    public ResponseEntity<FindRecruitmentDetailByVolunteerResponse> findRecruitmentByIdByVolunteer(
        @PathVariable Long recruitmentId) {
        return ResponseEntity.ok(recruitmentService.findRecruitmentByIdByVolunteer(recruitmentId));
    }

    @GetMapping("/volunteers/recruitments/{recruitmentId}/shelters")
    public ResponseEntity<FindShelterSimpleResponse> findShelterByVolunteerReview(
        @PathVariable Long recruitmentId) {
        return ResponseEntity.ok(recruitmentService.findShelterSimple(recruitmentId)
        );
    }

    @GetMapping("/volunteers/{volunteerId}/recruitments/completed")
    public ResponseEntity<FindCompletedRecruitmentsResponse> findCompletedRecruitments(
        @PathVariable("volunteerId") Long volunteerId,
        Pageable pageable) {
        FindCompletedRecruitmentsResponse response = recruitmentService.findCompletedRecruitments(
            volunteerId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/volunteers/recruitments")
    public ResponseEntity<FindRecruitmentsByVolunteerResponse> findRecruitmentsByVolunteer(
        @ModelAttribute @Valid FindRecruitmentsByVolunteerRequest findRecruitmentsByVolunteerRequest,
        Pageable pageable) {
        return ResponseEntity.ok(recruitmentService.findRecruitmentsByVolunteer(
            findRecruitmentsByVolunteerRequest.keyword(),
            findRecruitmentsByVolunteerRequest.startDate(),
            findRecruitmentsByVolunteerRequest.endDate(),
            findRecruitmentsByVolunteerRequest.isClosed(),
            findRecruitmentsByVolunteerRequest.title(),
            findRecruitmentsByVolunteerRequest.content(),
            findRecruitmentsByVolunteerRequest.shelterName(),
            pageable
        ));
    }

    @GetMapping("/shelters/recruitments")
    public ResponseEntity<FindRecruitmentsByShelterResponse> findRecruitmentsByShelter(
        @LoginUser Long shelterId,
        @ModelAttribute @Valid FindRecruitmentsByShelterRequest findRecruitmentsByShelterRequest,
        Pageable pageable
    ) {
        return ResponseEntity.ok(recruitmentService.findRecruitmentsByShelter(
            shelterId,
            findRecruitmentsByShelterRequest.keyword(),
            findRecruitmentsByShelterRequest.startDate(),
            findRecruitmentsByShelterRequest.endDate(),
            findRecruitmentsByShelterRequest.content(),
            findRecruitmentsByShelterRequest.title(),
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
}
