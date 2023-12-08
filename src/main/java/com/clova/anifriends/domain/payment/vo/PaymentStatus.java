package com.clova.anifriends.domain.payment.vo;

import com.clova.anifriends.domain.common.EnumType;

public enum PaymentStatus implements EnumType {
    PENDING,
    DONE,
    CANCELED,
    ;

    @Override
    public String getName() {
        return this.name();
    }
}
