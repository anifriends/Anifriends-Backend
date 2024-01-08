package com.clova.anifriends.domain.payment.service;

import com.clova.anifriends.domain.payment.dto.response.TossPaymentApiResponse;
import com.clova.anifriends.domain.payment.exception.PaymentFailException;
import com.clova.anifriends.domain.payment.vo.PaymentStatus;
import com.clova.anifriends.global.infrastructure.ApiService;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentClient {

    @Value("${payment.toss.secret-key}")
    private String secretKey;

    @Value("${payment.toss.confirm-url}")
    private String confirmUrl;

    private final ApiService apiService;

    public void confirmPayment(String orderId, String paymentKey, Integer amount) {
        HttpHeaders httpHeaders = getHttpHeaders();
        Map<String, Object> params = getParams(orderId, paymentKey, amount);
        TossPaymentApiResponse paymentApiResponse = requestPaymentApi(httpHeaders, params);

        validatePaymentResult(paymentApiResponse);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(getEncodeAuth());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return httpHeaders;
    }

    private String getEncodeAuth() {
        return new String(
            Base64.getEncoder()
                .encode((secretKey + ":").getBytes(StandardCharsets.UTF_8))
        );
    }

    private Map<String, Object> getParams(String uuid, String paymentKey, Integer amount) {
        Map<String, Object> params = new HashMap<>();
        params.put("paymentKey", paymentKey);
        params.put("orderId", uuid);
        params.put("amount", amount);

        return params;
    }

    private TossPaymentApiResponse requestPaymentApi(HttpHeaders httpHeaders,
        Map<String, Object> params) {
        return apiService.post(
            new HttpEntity<>(params, httpHeaders),
            confirmUrl,
            TossPaymentApiResponse.class
        );
    }

    private void validatePaymentResult(TossPaymentApiResponse paymentApiResponse) {
        if (!paymentApiResponse.status().equals(PaymentStatus.DONE.name())) {
            throw new PaymentFailException("결제가 실패되었습니다.");
        }
    }

}
