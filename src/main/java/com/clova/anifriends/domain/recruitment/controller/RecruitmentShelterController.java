package com.clova.anifriends.domain.recruitment.controller;

import com.clova.anifriends.domain.recruitment.dto.RecruitmentRequest;
import com.clova.anifriends.domain.recruitment.dto.RegisterRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.service.RecruitmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shelters/")
public class RecruitmentShelterController {

    private final RecruitmentService recruitmentService;

    @PostMapping("/recruitments")
    public ResponseEntity<RegisterRecruitmentResponse> registerRecruitment(
        @RequestBody @Valid RecruitmentRequest request) {
        return null;
    }
}
