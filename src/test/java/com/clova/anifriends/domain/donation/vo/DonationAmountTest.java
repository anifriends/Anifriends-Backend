package com.clova.anifriends.domain.donation.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.clova.anifriends.domain.donation.exception.DonationBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DonationAmountTest {

    @Nested
    @DisplayName("DonationAmount 생성 시")
    class NewDonationAmount {

        @Test
        @DisplayName("성공")
        void newDonationAmount() {
            //given
            Integer amount = 1000;

            //when
            DonationAmount donationAmount = new DonationAmount(amount);

            //then
            assertEquals(donationAmount.getAmount(), amount);
        }

        @Test
        @DisplayName("예외(DonationBadRequestException): 입력값이 null")
        void exceptionWhenAmountIsNull() {
            //given
            Integer nullAmount = null;

            //when
            Exception exception = catchException(() -> new DonationAmount(nullAmount));

            //then
            assertThat(exception).isInstanceOf(DonationBadRequestException.class);
        }

        @Test
        @DisplayName("예외(DonationBadRequestException): 금액이 1000원 미민일 경우")
        void exceptionWhenAmountIsUnder1000() {
            //given
            Integer amountUnder1000 = 999;

            //when
            Exception exception = catchException(() -> new DonationAmount(amountUnder1000));

            //then
            assertThat(exception).isInstanceOf(DonationBadRequestException.class);
        }
    }

}
