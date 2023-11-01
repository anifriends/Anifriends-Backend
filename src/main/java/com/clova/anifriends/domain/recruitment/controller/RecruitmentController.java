package com.clova.anifriends.domain.recruitment.controller;

import com.clova.anifriends.domain.auth.resolver.LoginUser;
import com.clova.anifriends.domain.recruitment.dto.RegisterRecruitmentRequest;
import com.clova.anifriends.domain.recruitment.dto.RegisterRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.service.RecruitmentService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
