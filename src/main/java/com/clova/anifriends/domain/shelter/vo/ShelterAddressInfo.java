package com.clova.anifriends.domain.shelter.vo;

import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.text.MessageFormat;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShelterAddressInfo {

    private static final int MIN_ADDRESS_LENGTH = 1;
    private static final int MIN_ADDRESS_DETAIL_LENGTH = 1;
    private static final int MAX_ADDRESS_LENGTH = 100;
    private static final int MAX_ADDRESS_DETAIL_LENGTH = 100;

    @Column(name = "address")
    private String address;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "is_opened_address")
    private boolean isOpenedAddress;

    public ShelterAddressInfo(String address, String addressDetail, boolean isOpenedAddress) {
        validateNotNull(address, addressDetail);
        validateAddress(address);
        validateAddressDetail(addressDetail);
        this.address = address;
        this.addressDetail = addressDetail;
        this.isOpenedAddress = isOpenedAddress;
    }

    public ShelterAddressInfo update(String address, String addressDetail,
        boolean isOpenedAddress) {
        return new ShelterAddressInfo(address, addressDetail, isOpenedAddress);
    }

    public ShelterAddressInfo updateAddressStatus(
        Boolean isOpenedAddress
    ) {
        validateAddressStatusNotNull(isOpenedAddress);

        return new ShelterAddressInfo(
            this.address,
            this.addressDetail,
            isOpenedAddress
        );
    }

    private void validateAddressStatusNotNull(
        Boolean isOpenedAddress
    ) {
        if (Objects.isNull(isOpenedAddress)) {
            throw new ShelterBadRequestException("보호소 상세 주소 공개 여부는 필수값입니다.");
        }
    }

    private void validateNotNull(String address, String addressDetail) {
        if (Objects.isNull(address)) {
            throw new ShelterBadRequestException("보호소 주소는 필수값입니다.");
        }
        if (Objects.isNull(addressDetail)) {
            throw new ShelterBadRequestException("보호소 상세 주소는 필수값입니다.");
        }
    }

    private void validateAddress(String address) {
        if (address.isBlank() || address.length() > MAX_ADDRESS_LENGTH) {
            throw new ShelterBadRequestException(
                MessageFormat.format("보호소 주소는 {0}자 이상, {1}자 이하여야 합니다.",
                    MIN_ADDRESS_LENGTH, MAX_ADDRESS_LENGTH));
        }
    }

    private void validateAddressDetail(String addressDetail) {
        if (addressDetail.isBlank() || addressDetail.length() > MAX_ADDRESS_DETAIL_LENGTH) {
            throw new ShelterBadRequestException(
                MessageFormat.format("보호소 상세 주소는 {0}자 이상, {1}자 이하여야 합니다.",
                    MIN_ADDRESS_DETAIL_LENGTH, MAX_ADDRESS_DETAIL_LENGTH));
        }
    }
}
