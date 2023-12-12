package com.clova.anifriends.domain.payment.service;

import com.clova.anifriends.domain.payment.Payment;
import com.clova.anifriends.domain.payment.dto.PaymentBadRequestException;
import com.clova.anifriends.domain.payment.dto.PaymentResponse;
import com.clova.anifriends.domain.payment.repository.PaymentRepository;
import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentClient paymentClient;

    @Transactional
    public PaymentResponse paySuccess(String orderId, String paymentKey, Integer amount) {
        Payment payment = getPayment(orderId);
        validatePaymentStatus(payment);
        validatePaymentAmount(payment, amount);

        paymentClient.confirmPayment(orderId, paymentKey, amount);

        payment.updatePaymentKey(paymentKey);
        payment.success();

        return new PaymentResponse(payment.getStatus(), null);
    }

    @Transactional
    public PaymentResponse payFail(String orderId, String message) {
        Payment payment = getPayment(orderId);
        validatePaymentStatus(payment);

        payment.fail();

        return new PaymentResponse(payment.getStatus(), message);

    }

    private void validatePaymentStatus(Payment payment) {
        if (payment.isFail() || payment.isSuccess()) {
            throw new PaymentBadRequestException(
                MessageFormat.format("이미 처리된 결제입니다. 결제 상태: {0}", payment.getStatus()));
        }
    }

    private void validatePaymentAmount(Payment payment, Integer amount) {
        if (payment.isDifferentAmount(amount)) {
            throw new PaymentBadRequestException("결제 금액이 일치하지 않습니다");
        }
    }

    private Payment getPayment(String orderId) {
        return paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new RuntimeException("결제 정보가 존재하지 않습니다."));
    }
}
