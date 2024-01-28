package com.clova.anifriends.domain.payment.service;

import static com.clova.anifriends.domain.payment.vo.PaymentStatus.ABORTED;
import static com.clova.anifriends.domain.payment.vo.PaymentStatus.DONE;

import com.clova.anifriends.domain.payment.Payment;
import com.clova.anifriends.domain.payment.dto.response.PaymentResponse;
import com.clova.anifriends.domain.payment.exception.PaymentBadRequestException;
import com.clova.anifriends.domain.payment.exception.PaymentFailException;
import com.clova.anifriends.domain.payment.repository.PaymentRepository;
import com.clova.anifriends.global.exception.ExternalApiException;
import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentClient paymentClient;

    public PaymentResponse paySuccess(String orderId, String paymentKey, Integer amount) {
        Payment payment = getPayment(orderId);
        validatePaymentStatus(payment);
        validatePaymentAmount(payment, amount);

        try {
            paymentClient.confirmPayment(orderId, paymentKey, amount);
        } catch (PaymentFailException | ExternalApiException exception) {
            paymentRepository.updatePaymentStatus(payment.getPaymentId(), ABORTED);
            throw exception;
        }

        paymentRepository.updatePaymentKeyAndStatus(payment.getPaymentId(), paymentKey, DONE);

        return new PaymentResponse(DONE, null);
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
