package com.clova.anifriends.domain.donation.vo;

import com.clova.anifriends.domain.donation.exception.DonationBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.text.MessageFormat;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class DonationAmount {

    private static final int MIN_AMOUNT = 1000;

    @Column(name = "amount")
    private Integer amount;

    public DonationAmount(Integer amount) {
        validateAmount(amount);
        this.amount = amount;
    }

    public boolean isDifferent(Integer amount) {
        return !this.amount.equals(amount);
    }

    private void validateAmount(Integer amount) {
        validateNotNull(amount);
        validateSize(amount);
    }

    private void validateNotNull(Integer amount) {
        if (Objects.isNull(amount)) {
            throw new DonationBadRequestException("기부 금액은 필수 값 입니다.");
        }
    }

    private void validateSize(int amount) {
        if (amount < MIN_AMOUNT) {
            throw new DonationBadRequestException(
                MessageFormat.format("기부 금액은 {0}원 이상이어야 합니다.", MIN_AMOUNT));
        }
    }
}
