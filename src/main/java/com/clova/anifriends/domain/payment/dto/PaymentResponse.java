package com.clova.anifriends.domain.payment.dto;

import com.clova.anifriends.domain.payment.vo.PaymentStatus;

public record PaymentResponse(PaymentStatus status, String message) {

}
