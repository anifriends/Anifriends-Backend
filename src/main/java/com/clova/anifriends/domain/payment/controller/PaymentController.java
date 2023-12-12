package com.clova.anifriends.domain.payment.controller;

import com.clova.anifriends.domain.payment.dto.PaymentResponse;
import com.clova.anifriends.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/success")
    public ResponseEntity<PaymentResponse> paySuccess(
        @RequestParam("orderId") String orderId,
        @RequestParam("paymentKey") String paymentKey,
        @RequestParam("amount") Integer amount
    ) {
        return ResponseEntity.ok(
            paymentService.paySuccess(orderId, paymentKey, amount));
    }

    @GetMapping("/fail")
    public ResponseEntity<PaymentResponse> payFail(
        @RequestParam("orderId") String orderId,
        @RequestParam("message") String message
    ) {
        return ResponseEntity.ok(
            paymentService.payFail(orderId, message));
    }

}
