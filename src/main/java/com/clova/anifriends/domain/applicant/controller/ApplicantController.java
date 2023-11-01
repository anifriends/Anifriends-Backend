package com.clova.anifriends.domain.applicant.controller;

import com.clova.anifriends.domain.applicant.service.ApplicantService;
import com.clova.anifriends.domain.auth.resolver.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApplicantController {

    private final ApplicantService applicantService;

    @PostMapping("/recruitments/{recruitmentId}/apply")
    public ResponseEntity<Void> registerApplicant(
        @PathVariable Long recruitmentId,
        @LoginUser Long volunteerId
    ) {
        applicantService.registerApplicant(recruitmentId, volunteerId);
        return ResponseEntity.noContent().build();
    }
}
