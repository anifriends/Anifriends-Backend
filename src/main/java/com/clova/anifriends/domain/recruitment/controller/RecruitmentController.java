package com.clova.anifriends.domain.recruitment.controller;

import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentByShelterResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentByVolunteerResponse;
import com.clova.anifriends.domain.recruitment.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @GetMapping("/shelters/recruitments/{recruitmentId}")
    public ResponseEntity<FindRecruitmentByShelterResponse> findRecruitmentByIdByShelter(
        @PathVariable Long recruitmentId) {
        return ResponseEntity.ok(recruitmentService.findRecruitmentByIdByShelter(recruitmentId));
    }

    @GetMapping("/volunteers/recruitments/{recruitmentId}")
    public ResponseEntity<FindRecruitmentByVolunteerResponse> findRecruitmentByIdByVolunteer(
        @PathVariable Long recruitmentId) {
        return ResponseEntity.ok(recruitmentService.findRecruitmentByIdByVolunteer(recruitmentId));
    }
}
