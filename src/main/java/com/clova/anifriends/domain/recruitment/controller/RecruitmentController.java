package com.clova.anifriends.domain.recruitment.controller;

import com.clova.anifriends.domain.auth.resolver.LoginUser;
import com.clova.anifriends.domain.recruitment.dto.request.RegisterRecruitmentRequest;
import com.clova.anifriends.domain.recruitment.dto.response.RegisterRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentByShelterResponse;
import com.clova.anifriends.domain.recruitment.service.RecruitmentService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        RegisterRecruitmentResponse response
            = recruitmentService.registerRecruitment(userId, request);
        URI location = URI.create("/api/shelters/recruitments/" + response.recruitmentId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/shelters/recruitments/{recruitmentId}")
    public ResponseEntity<FindRecruitmentByShelterResponse> findRecruitmentByIdByShelter(
        @PathVariable Long recruitmentId) {
        return ResponseEntity.ok(recruitmentService.findRecruitmentByIdByShelter(recruitmentId));
    }
}
