package com.clova.anifriends.domain.payment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.donation.Donation;
import com.clova.anifriends.domain.donation.support.fixture.DonationFixture;
import com.clova.anifriends.domain.payment.Payment;
import com.clova.anifriends.domain.payment.vo.PaymentStatus;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PaymentRepositoryTest extends BaseRepositoryTest {

    @Nested
    @DisplayName("updatePaymentStatus 실행 시")
    class UpdatePaymentStatusTest {

        @Test
        @DisplayName("성공")
        void updatePaymentStatus() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            Volunteer volunteer = VolunteerFixture.volunteer();
            Donation donation = DonationFixture.donation(shelter, volunteer);
            Payment payment = new Payment(donation);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            paymentRepository.save(payment);

            // when
            paymentRepository.updatePaymentStatus(payment.getPaymentId(), PaymentStatus.ABORTED);

            // then
            entityManager.flush();
            entityManager.clear();

            Payment updatedPayment = paymentRepository.findById(payment.getPaymentId()).get();
            assertThat(updatedPayment.getStatus()).isEqualTo(PaymentStatus.ABORTED);
        }
    }

}