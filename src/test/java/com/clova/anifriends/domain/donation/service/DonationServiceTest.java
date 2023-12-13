package com.clova.anifriends.domain.donation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clova.anifriends.domain.donation.Donation;
import com.clova.anifriends.domain.donation.dto.response.PaymentRequestResponse;
import com.clova.anifriends.domain.donation.support.fixture.DonationFixture;
import com.clova.anifriends.domain.payment.Payment;
import com.clova.anifriends.domain.payment.repository.PaymentRepository;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DonationServiceTest {

    @InjectMocks
    private DonationService donationService;

    @Mock
    private ShelterRepository shelterRepository;

    @Mock
    private VolunteerRepository volunteerRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Nested
    @DisplayName("registerDonation 실행 시")
    class RegisterDonationTest {

        @Test
        @DisplayName("성공")
        void registerDonation() {
            //given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();
            Donation donation = DonationFixture.donation(shelter, volunteer);
            Payment payment = new Payment(donation);
            PaymentRequestResponse expected = PaymentRequestResponse.of(payment);

            when(shelterRepository.findById(anyLong())).thenReturn(Optional.of(shelter));
            when(volunteerRepository.findById(anyLong())).thenReturn(Optional.of(volunteer));

            //when
            PaymentRequestResponse result = donationService.registerDonation(1L, 1L, 1000);

            //then
            verify(paymentRepository).save(any(Payment.class));
            assertThat(result.orderId()).isNotNull();
            assertThat(result).usingRecursiveComparison().ignoringFields("orderId")
                .isEqualTo(expected);
        }
    }

}
