package com.clova.anifriends.domain.payment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.clova.anifriends.domain.payment.dto.response.TossPaymentApiResponse;
import com.clova.anifriends.domain.payment.exception.PaymentFailException;
import com.clova.anifriends.domain.payment.vo.PaymentStatus;
import com.clova.anifriends.global.infrastructure.ApiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentClientTest {

    @InjectMocks
    private PaymentClient paymentClient;

    @Mock
    private ApiService apiService;

    @Nested
    @DisplayName("confirmPayment 실행 시")
    class ConfirmPaymentTest {

        @Test
        @DisplayName("성공")
        void confirmPayment() {
            // given
            String orderId = "mockOrderId";
            String paymentKey = "mockPaymentKey";
            int amount = 1000;
            TossPaymentApiResponse paymentApiResponse = new TossPaymentApiResponse(
                PaymentStatus.DONE.name());

            when(apiService.post(any(), any(), any())).thenReturn(paymentApiResponse);

            // when
            Exception exception = catchException(
                () -> paymentClient.confirmPayment(orderId, paymentKey, amount));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("예외(PaymentFailException): 결제 실패")
        void exceptionWhenPayFail() {
            // given
            String orderId = "mockOrderId";
            String paymentKey = "mockPaymentKey";
            int amount = 1000;
            TossPaymentApiResponse paymentApiResponse = new TossPaymentApiResponse(
                PaymentStatus.ABORTED.name());

            when(apiService.post(any(), any(), any())).thenReturn(paymentApiResponse);

            // when
            Exception exception = catchException(
                () -> paymentClient.confirmPayment(orderId, paymentKey, amount));

            // then
            assertThat(exception).isInstanceOf(PaymentFailException.class);
        }
    }

}