package com.clova.anifriends.domain.applicant.controller;

import com.clova.anifriends.domain.applicant.dto.FindApplicantsApprovedResponse;
import com.clova.anifriends.domain.applicant.dto.FindApplyingVolunteersResponse;
import com.clova.anifriends.domain.applicant.service.ApplicantService;
import com.clova.anifriends.domain.auth.resolver.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApplicantController {

    private final ApplicantService applicantService;

    @PostMapping("/volunteers/recruitments/{recruitmentId}/apply")
    public ResponseEntity<Void> registerApplicant(
        @PathVariable Long recruitmentId,
        @LoginUser Long volunteerId
    ) {
        applicantService.registerApplicant(recruitmentId, volunteerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/volunteers/applicants")
    public ResponseEntity<FindApplyingVolunteersResponse> findApplyingVolunteers(
        @LoginUser Long volunteerId
    ) {
        return ResponseEntity.ok(applicantService.findApplyingVolunteers(volunteerId));

    }

    @GetMapping("/shelters/recruitments/{recruitmentId}/approval")
    public ResponseEntity<FindApplicantsApprovedResponse> findApplicantApproved(
        @LoginUser Long shelterId,
        @PathVariable Long recruitmentId
    ) {
        return ResponseEntity.ok(applicantService.findApplicantsApproved(shelterId, recruitmentId));
    }
}
