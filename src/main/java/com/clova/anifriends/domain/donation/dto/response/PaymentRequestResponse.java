package com.clova.anifriends.domain.donation.dto.response;

import com.clova.anifriends.domain.payment.Payment;

public record PaymentRequestResponse(
    int amount,
    String orderId,
    String orderName,
    String customerEmail,
    String customerName
) {

    public static PaymentRequestResponse of(Payment payment) {
        return new PaymentRequestResponse(
            payment.getDonation().getAmount(),
            payment.getOrderId(),
            payment.getOrderName(),
            payment.getDonation().getVolunteer().getEmail(),
            payment.getDonation().getVolunteer().getName()
        );

    }

}
