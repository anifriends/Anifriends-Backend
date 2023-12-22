package com.clova.anifriends.domain.payment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.donation.Donation;
import com.clova.anifriends.domain.donation.support.fixture.DonationFixture;
import com.clova.anifriends.domain.payment.Payment;
import com.clova.anifriends.domain.payment.dto.response.TossPaymentApiResponse;
import com.clova.anifriends.domain.payment.exception.PaymentFailException;
import com.clova.anifriends.domain.payment.vo.PaymentStatus;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import com.clova.anifriends.global.exception.ExternalApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PaymentIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Nested
    @DisplayName("paySuccess 실행 시")
    class PaySuccessTest {

        @Test
        @DisplayName("성공: 결제 상태가 DONE 으로 변경")
        void paySuccess() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            Volunteer volunteer = VolunteerFixture.volunteer();
            Donation donation = DonationFixture.donation(shelter, volunteer);
            String mockPaymentKey = "mockPaymentKey";
            Payment payment = new Payment(donation);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            paymentRepository.save(payment);

            when(apiService.post(any(), any(), any())).thenReturn(
                new TossPaymentApiResponse("DONE"));

            // when
            paymentService.paySuccess(payment.getOrderId(), mockPaymentKey, donation.getAmount());

            // then
            Payment updatedPayment = paymentRepository.findById(payment.getPaymentId()).get();
            assertThat(updatedPayment.getStatus()).isEqualTo(PaymentStatus.DONE);
        }

        @Test
        @DisplayName("예외(ExternalApiException): PG Api 예외 발생 시 Payment 상태 ABORTED 로 변경")
        void exceptionWhenPGApiException() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            Volunteer volunteer = VolunteerFixture.volunteer();
            Donation donation = DonationFixture.donation(shelter, volunteer);
            String mockPaymentKey = "mockPaymentKey";
            Payment payment = new Payment(donation);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            paymentRepository.save(payment);

            when(apiService.post(any(), any(), any())).thenThrow(
                new ExternalApiException("errorMessage"));

            // when
            Exception exception = catchException(
                () -> paymentService.paySuccess(payment.getOrderId(), mockPaymentKey,
                    donation.getAmount()));

            // then
            Payment updatedPayment = paymentRepository.findById(payment.getPaymentId()).get();
            assertThat(updatedPayment.getStatus()).isEqualTo(PaymentStatus.ABORTED);
            assertThat(exception).isInstanceOf(ExternalApiException.class);
        }

        @Test
        @DisplayName("예외(PaymentFailException): 결제 실패 시 Payment 상태 ABORTED 로 변경")
        void exceptionWhenPaymentFail() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            Volunteer volunteer = VolunteerFixture.volunteer();
            Donation donation = DonationFixture.donation(shelter, volunteer);
            String mockPaymentKey = "mockPaymentKey";
            Payment payment = new Payment(donation);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            paymentRepository.save(payment);

            when(apiService.post(any(), any(), any())).thenReturn(
                new TossPaymentApiResponse("FAIL"));

            // when
            Exception exception = catchException(
                () -> paymentService.paySuccess(payment.getOrderId(), mockPaymentKey,
                    donation.getAmount()));

            // then
            Payment updatedPayment = paymentRepository.findById(payment.getPaymentId()).get();
            assertThat(updatedPayment.getStatus()).isEqualTo(PaymentStatus.ABORTED);
            assertThat(exception).isInstanceOf(PaymentFailException.class);
        }
    }

}
