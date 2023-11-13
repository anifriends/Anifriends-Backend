package com.clova.anifriends.domain.shelter.controller;

import com.clova.anifriends.domain.auth.LoginUser;
import com.clova.anifriends.domain.shelter.dto.request.CheckDuplicateShelterEmailRequest;
import com.clova.anifriends.domain.shelter.dto.request.RegisterShelterRequest;
import com.clova.anifriends.domain.shelter.dto.request.UpdateAddressStatusRequest;
import com.clova.anifriends.domain.shelter.dto.request.UpdateShelterPasswordRequest;
import com.clova.anifriends.domain.shelter.dto.request.UpdateShelterRequest;
import com.clova.anifriends.domain.shelter.dto.response.CheckDuplicateShelterResponse;
import com.clova.anifriends.domain.shelter.dto.response.FindShelterDetailResponse;
import com.clova.anifriends.domain.shelter.dto.response.FindShelterMyPageResponse;
import com.clova.anifriends.domain.shelter.dto.response.FindShelterSimpleByVolunteerResponse;
import com.clova.anifriends.domain.shelter.service.ShelterService;
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
public class ShelterController {

    private final ShelterService shelterService;

    @PostMapping("/shelters/email")
    public ResponseEntity<CheckDuplicateShelterResponse> checkDuplicateShelterEmail(
        @RequestBody @Valid CheckDuplicateShelterEmailRequest checkDuplicateShelterEmailRequest) {
        CheckDuplicateShelterResponse checkDuplicateShelterResponse
            = shelterService.checkDuplicateEmail(checkDuplicateShelterEmailRequest.email());
        return ResponseEntity.ok(checkDuplicateShelterResponse);
    }

    @PostMapping("/shelters")
    public ResponseEntity<Void> registerShelter(
        @RequestBody @Valid RegisterShelterRequest registerShelterRequest) {
        Long shelterId = shelterService.registerShelter(
            registerShelterRequest.email(),
            registerShelterRequest.password(),
            registerShelterRequest.name(),
            registerShelterRequest.address(),
            registerShelterRequest.addressDetail(),
            registerShelterRequest.phoneNumber(),
            registerShelterRequest.sparePhoneNumber(),
            registerShelterRequest.isOpenedAddress());
        URI location = URI.create("/api/shelters/" + shelterId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/volunteers/shelters/{shelterId}/profile")
    public ResponseEntity<FindShelterDetailResponse> findShelterDetail(
        @PathVariable Long shelterId
    ) {
        return ResponseEntity.ok()
            .body(shelterService.findShelterDetail(
                shelterId
            ));
    }

    @GetMapping("/volunteers/shelters/{shelterId}/profile/simple")
    public ResponseEntity<FindShelterSimpleByVolunteerResponse> findShelterSimpleByVolunteer(
        @PathVariable Long shelterId
    ) {
        return ResponseEntity.ok(shelterService.findShelterSimpleByVolunteer(shelterId));
    }

    @GetMapping("/shelters/me")
    public ResponseEntity<FindShelterMyPageResponse> findShelterMyPage(@LoginUser Long shelterId) {
        return ResponseEntity.ok(shelterService.findShelterMyPage(shelterId));
    }

    @PatchMapping("/shelters/me/passwords")
    public ResponseEntity<Void> updatePassword(
        @LoginUser Long shelterId,
        @RequestBody @Valid UpdateShelterPasswordRequest updateShelterPasswordRequest) {
        shelterService.updatePassword(
            shelterId,
            updateShelterPasswordRequest.oldPassword(),
            updateShelterPasswordRequest.newPassword());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/shelters/me/address/status")
    public ResponseEntity<Void> updateShelterAddressStatus(
        @LoginUser Long shelterId,
        @RequestBody @Valid UpdateAddressStatusRequest updateAddressStatusRequest
    ) {
        shelterService.updateAddressStatus(shelterId, updateAddressStatusRequest.isOpenedAddress());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/shelters/me")
    public ResponseEntity<Void> updateShelter(
        @LoginUser Long shelterId,
        @RequestBody UpdateShelterRequest updateShelterRequest
    ) {
        shelterService.updateShelter(
            shelterId,
            updateShelterRequest.name(),
            updateShelterRequest.imageUrl(),
            updateShelterRequest.address(),
            updateShelterRequest.addressDetail(),
            updateShelterRequest.phoneNumber(),
            updateShelterRequest.sparePhoneNumber(),
            updateShelterRequest.isOpenedAddress()
        );

        return ResponseEntity.noContent().build();
    }
}
