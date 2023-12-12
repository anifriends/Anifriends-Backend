package com.clova.anifriends.domain.payment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clova.anifriends.domain.donation.Donation;
import com.clova.anifriends.domain.donation.support.fixture.DonationFixture;
import com.clova.anifriends.domain.payment.Payment;
import com.clova.anifriends.domain.payment.dto.response.PaymentResponse;
import com.clova.anifriends.domain.payment.exception.PaymentBadRequestException;
import com.clova.anifriends.domain.payment.repository.PaymentRepository;
import com.clova.anifriends.domain.payment.vo.PaymentStatus;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentClient paymentClient;

    @Nested
    @DisplayName("paySuccess 실행 시")
    class PaySuccessTest {

        @Test
        @DisplayName("성공")
        void paySuccess() {
            // given
            String orderId = "mockOrderId";
            String paymentKey = "mockPaymentKey";

            Shelter shelter = ShelterFixture.shelter();
            Volunteer volunteer = VolunteerFixture.volunteer();

            Donation donation = DonationFixture.donation(shelter, volunteer);
            int amount = donation.getAmount();
            Payment payment = new Payment(donation);

            PaymentResponse expected = new PaymentResponse(PaymentStatus.DONE, null);

            when(paymentRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(payment));

            // when
            PaymentResponse result = paymentService.paySuccess(orderId, paymentKey, amount);

            // then
            verify(paymentClient, times(1)).confirmPayment(orderId, paymentKey, amount);
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.DONE);
            assertThat(payment.getPaymentKey()).isEqualTo(paymentKey);
        }

        @Test
        @DisplayName("예외(PaymentBadRequestException): 이미 실패 처리된 결제")
        void exceptionWhenAlreadyFailPayment() {
            // given
            String orderId = "mockOrderId";
            String paymentKey = "mockPaymentKey";

            Shelter shelter = ShelterFixture.shelter();
            Volunteer volunteer = VolunteerFixture.volunteer();

            Donation donation = DonationFixture.donation(shelter, volunteer);
            int amount = donation.getAmount();
            Payment failPayment = new Payment(donation);
            ReflectionTestUtils.setField(failPayment, "status", PaymentStatus.ABORTED);

            when(paymentRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(failPayment));

            // when
            Exception exception = catchException(
                () -> paymentService.paySuccess(orderId, paymentKey, amount));

            // then
            assertThat(exception).isInstanceOf(PaymentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(PaymentBadRequestException): 이미 성공 처리된 결제")
        void exceptionWhenAlreadySuccessPayment() {
            // given
            String orderId = "mockOrderId";
            String paymentKey = "mockPaymentKey";

            Shelter shelter = ShelterFixture.shelter();
            Volunteer volunteer = VolunteerFixture.volunteer();

            Donation donation = DonationFixture.donation(shelter, volunteer);
            int amount = donation.getAmount();
            Payment successPayment = new Payment(donation);
            ReflectionTestUtils.setField(successPayment, "status", PaymentStatus.DONE);

            when(paymentRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(successPayment));

            // when
            Exception exception = catchException(
                () -> paymentService.paySuccess(orderId, paymentKey, amount));

            // then
            assertThat(exception).isInstanceOf(PaymentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(PaymentBadRequestException): 금액이 일치하지 않음")
        void exceptionWhenIncorrectAmount() {
            // given
            String orderId = "mockOrderId";
            String paymentKey = "mockPaymentKey";

            Shelter shelter = ShelterFixture.shelter();
            Volunteer volunteer = VolunteerFixture.volunteer();

            Donation donation = DonationFixture.donation(shelter, volunteer);
            int incorrectAmount = 123;
            Payment successPayment = new Payment(donation);
            ReflectionTestUtils.setField(successPayment, "status", PaymentStatus.DONE);

            when(paymentRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(successPayment));

            // when
            Exception exception = catchException(
                () -> paymentService.paySuccess(orderId, paymentKey, incorrectAmount));

            // then
            assertThat(exception).isInstanceOf(PaymentBadRequestException.class);
        }
    }

    @Nested
    @DisplayName("payFail 실행 시")
    class PayFailTest {

        @Test
        @DisplayName("성공")
        void payFail() {
            // given
            String orderId = "mockOrderId";
            String message = "mockMessage";

            Shelter shelter = ShelterFixture.shelter();
            Volunteer volunteer = VolunteerFixture.volunteer();

            Donation donation = DonationFixture.donation(shelter, volunteer);
            Payment payment = new Payment(donation);

            PaymentResponse expected = new PaymentResponse(PaymentStatus.ABORTED, message);

            when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(payment));

            // when
            PaymentResponse result = paymentService.payFail(orderId, message);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.ABORTED);
        }

        @Test
        @DisplayName("예외(PaymentBadRequestException): 이미 실패 처리된 결제")
        void exceptionWhenAlreadyFailPayment() {
            // given
            String orderId = "mockOrderId";
            String message = "mockMessage";

            Shelter shelter = ShelterFixture.shelter();
            Volunteer volunteer = VolunteerFixture.volunteer();

            Donation donation = DonationFixture.donation(shelter, volunteer);
            Payment failPayment = new Payment(donation);
            ReflectionTestUtils.setField(failPayment, "status", PaymentStatus.ABORTED);

            when(paymentRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(failPayment));

            // when
            Exception exception = catchException(
                () -> paymentService.payFail(orderId, message));

            // then
            assertThat(exception).isInstanceOf(PaymentBadRequestException.class);
        }

        @Test
        @DisplayName("예외(PaymentBadRequestException): 이미 성공 처리된 결제")
        void exceptionWhenAlreadySuccessPayment() {
            // given
            String orderId = "mockOrderId";
            String message = "mockMessage";

            Shelter shelter = ShelterFixture.shelter();
            Volunteer volunteer = VolunteerFixture.volunteer();

            Donation donation = DonationFixture.donation(shelter, volunteer);
            Payment successPayment = new Payment(donation);
            ReflectionTestUtils.setField(successPayment, "status", PaymentStatus.DONE);

            when(paymentRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(successPayment));

            // when
            Exception exception = catchException(
                () -> paymentService.payFail(orderId, message));

            // then
            assertThat(exception).isInstanceOf(PaymentBadRequestException.class);
        }
    }
}
