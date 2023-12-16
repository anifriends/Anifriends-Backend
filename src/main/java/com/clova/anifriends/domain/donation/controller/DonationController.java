package com.clova.anifriends.domain.donation.controller;

import com.clova.anifriends.domain.auth.LoginUser;
import com.clova.anifriends.domain.auth.authorization.VolunteerOnly;
import com.clova.anifriends.domain.donation.dto.request.RegisterDonationRequest;
import com.clova.anifriends.domain.donation.dto.response.PaymentRequestResponse;
import com.clova.anifriends.domain.donation.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/volunteers/donations")
public class DonationController {

    private final DonationService donationService;

    @PostMapping
    @VolunteerOnly
    public ResponseEntity<PaymentRequestResponse> registerDonation(
        @RequestBody RegisterDonationRequest registerDonationRequest,
        @LoginUser Long volunteerId
    ) {
        return ResponseEntity.ok(
            donationService.registerDonation(volunteerId, registerDonationRequest.shelterId(),
                registerDonationRequest.amount()));
    }

}
