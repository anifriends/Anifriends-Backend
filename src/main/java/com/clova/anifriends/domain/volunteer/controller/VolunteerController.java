package com.clova.anifriends.domain.volunteer.controller;

import com.clova.anifriends.domain.auth.LoginUser;
import com.clova.anifriends.domain.volunteer.dto.request.CheckDuplicateVolunteerEmailRequest;
import com.clova.anifriends.domain.volunteer.dto.request.RegisterVolunteerRequest;
import com.clova.anifriends.domain.volunteer.dto.request.UpdateVolunteerInfoRequest;
import com.clova.anifriends.domain.volunteer.dto.request.UpdateVolunteerPasswordRequest;
import com.clova.anifriends.domain.volunteer.dto.response.CheckDuplicateVolunteerEmailResponse;
import com.clova.anifriends.domain.volunteer.dto.response.FindVolunteerMyPageResponse;
import com.clova.anifriends.domain.volunteer.dto.response.FindVolunteerProfileResponse;
import com.clova.anifriends.domain.volunteer.dto.response.RegisterVolunteerResponse;
import com.clova.anifriends.domain.volunteer.service.VolunteerService;
import com.clova.anifriends.domain.auth.authorization.ShelterOnly;
import com.clova.anifriends.domain.auth.authorization.VolunteerOnly;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VolunteerController {

    private final VolunteerService volunteerService;
    private static final String BASE_URI = "/api/volunteers/";

    @PostMapping("/volunteers/email")
    public ResponseEntity<CheckDuplicateVolunteerEmailResponse> checkDuplicateVolunteerEmail(
        @RequestBody @Valid CheckDuplicateVolunteerEmailRequest checkDuplicateVolunteerEmailRequest) {
        String targetEmail = checkDuplicateVolunteerEmailRequest.email();
        CheckDuplicateVolunteerEmailResponse checkDuplicateVolunteerEmailResponse
            = volunteerService.checkDuplicateVolunteerEmail(targetEmail);
        return ResponseEntity.ok(checkDuplicateVolunteerEmailResponse);
    }

    @PostMapping("/volunteers")
    public ResponseEntity<RegisterVolunteerResponse> registerVolunteer(
        @RequestBody @Valid RegisterVolunteerRequest registerVolunteerRequest
    ) {
        RegisterVolunteerResponse registerVolunteerResponse = volunteerService.registerVolunteer(
            registerVolunteerRequest.email(),
            registerVolunteerRequest.password(),
            registerVolunteerRequest.name(),
            registerVolunteerRequest.birthDate(),
            registerVolunteerRequest.phoneNumber(),
            registerVolunteerRequest.gender()
        );
        URI location = URI.create(BASE_URI + registerVolunteerResponse.volunteerId());
        return ResponseEntity.created(location).body(registerVolunteerResponse);
    }

    @VolunteerOnly
    @GetMapping("/volunteers/me")
    public ResponseEntity<FindVolunteerMyPageResponse> findVolunteerMyPage(
        @LoginUser Long volunteerId
    ) {
        return ResponseEntity.ok(volunteerService.findVolunteerMyPage(volunteerId));
    }

    @ShelterOnly
    @GetMapping("/shelters/volunteers/{volunteerId}/profile")
    public ResponseEntity<FindVolunteerProfileResponse> findVolunteerProfile(
        @PathVariable Long volunteerId
    ) {
        return ResponseEntity.ok(
            volunteerService.findVolunteerProfile(
                volunteerId
            )
        );
    }

    @VolunteerOnly
    @PatchMapping("/volunteers/me")
    public ResponseEntity<Void> updateVolunteerInfo(
        @LoginUser Long volunteerId,
        @RequestBody @Valid UpdateVolunteerInfoRequest updateVolunteerInfoRequest) {
        volunteerService.updateVolunteerInfo(
            volunteerId,
            updateVolunteerInfoRequest.name(),
            updateVolunteerInfoRequest.gender(),
            updateVolunteerInfoRequest.birthDate(),
            updateVolunteerInfoRequest.phoneNumber(),
            updateVolunteerInfoRequest.imageUrl());
        return ResponseEntity.noContent().build();
    }

    @VolunteerOnly
    @PatchMapping("/volunteers/me/passwords")
    public ResponseEntity<Void> updateVolunteerPassword(
        @LoginUser Long volunteerId,
        @RequestBody @Valid UpdateVolunteerPasswordRequest updateVolunteerPasswordRequest
    ) {
        volunteerService.updatePassword(
            volunteerId,
            updateVolunteerPasswordRequest.oldPassword(),
            updateVolunteerPasswordRequest.newPassword()
        );

        return ResponseEntity.noContent().build();
    }
}
