package com.clova.anifriends.domain.donation.dto.response;

import com.clova.anifriends.domain.payment.Payment;
import java.util.UUID;

public record PaymentRequestResponse(
    int amount,
    UUID orderId,
    String orderName,
    String customerEmail,
    String customerName,
    String successUrl,
    String failUrl
) {

    public static PaymentRequestResponse from(
        Payment payment,
        String successUrl,
        String failUrl
    ) {
        return new PaymentRequestResponse(
            payment.getDonation().getAmount(),
            payment.getOrderId(),
            payment.getOrderName(),
            payment.getDonation().getVolunteer().getEmail(),
            payment.getDonation().getVolunteer().getName(),
            successUrl,
            failUrl
        );

    }

}
