package com.clova.anifriends.domain.payment.dto.response;

import com.clova.anifriends.domain.payment.vo.PaymentStatus;

public record PaymentResponse(PaymentStatus status, String message) {

}
