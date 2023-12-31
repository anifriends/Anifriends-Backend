package com.clova.anifriends.domain.donation.service;

import com.clova.anifriends.domain.donation.Donation;
import com.clova.anifriends.domain.donation.dto.response.PaymentRequestResponse;
import com.clova.anifriends.domain.donation.exception.DonationDuplicateException;
import com.clova.anifriends.domain.donation.repository.DonationCacheRepository;
import com.clova.anifriends.domain.payment.Payment;
import com.clova.anifriends.domain.payment.repository.PaymentRepository;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final ShelterRepository shelterRepository;
    private final VolunteerRepository volunteerRepository;
    private final PaymentRepository paymentRepository;
    private final DonationCacheRepository donationCacheRepository;

    @Transactional
    public PaymentRequestResponse registerDonation(Long volunteerId, Long shelterId,
        Integer amount) {
        checkDuplicate(volunteerId);

        Shelter shelter = getShelter(shelterId);
        Volunteer volunteer = getVolunteer(volunteerId);
        Donation donation = new Donation(shelter, volunteer, amount);
        Payment payment = new Payment(donation);
        paymentRepository.save(payment);

        return PaymentRequestResponse.of(payment);
    }

    private void checkDuplicate(Long volunteerId) {
        if (donationCacheRepository.isDuplicateDonation(volunteerId)) {
            throw new DonationDuplicateException("중복된 요청입니다.");
        }
    }

    private Volunteer getVolunteer(Long volunteerId) {
        return volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 봉사자입니다."));
    }

    private Shelter getShelter(Long shelterId) {
        return shelterRepository.findById(shelterId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 보호소입니다."));
    }
}
