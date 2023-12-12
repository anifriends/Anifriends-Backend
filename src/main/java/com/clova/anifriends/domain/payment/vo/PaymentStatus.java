package com.clova.anifriends.domain.payment.vo;

import com.clova.anifriends.domain.common.EnumType;

public enum PaymentStatus implements EnumType {
    PENDING,
    DONE,
    ABORTED,
    ;

    @Override
    public String getName() {
        return this.name();
    }

    public boolean isAborted() {
        return this == ABORTED;
    }

    public boolean isDone() {
        return this == DONE;
    }
}
