package com.clova.anifriends.domain.shelter.controller;

import com.clova.anifriends.domain.auth.resolver.LoginUser;
import com.clova.anifriends.domain.shelter.dto.FindShelterDetailResponse;
import com.clova.anifriends.domain.shelter.dto.FindShelterMyPageResponse;
import com.clova.anifriends.domain.shelter.service.ShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ShelterController {

    private final ShelterService shelterService;

    @GetMapping("/volunteers/shelters/{shelterId}/profile")
    public ResponseEntity<FindShelterDetailResponse> findShelterDetail(
        @PathVariable Long shelterId
    ) {
        return ResponseEntity.ok()
            .body(shelterService.findShelterDetail(
                shelterId
            ));
    }

    @GetMapping("/shelters/me")
    public ResponseEntity<FindShelterMyPageResponse> findShelterMyPage(
        @LoginUser Long shelterId
    ) {
        return ResponseEntity.ok()
            .body(shelterService.findShelterMyPage(
                shelterId
            ));
    }
}
