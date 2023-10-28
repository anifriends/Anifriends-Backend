package com.clova.anifriends.domain.shelter;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.shelter.wrapper.Address;
import com.clova.anifriends.domain.shelter.wrapper.AddressDetail;
import com.clova.anifriends.domain.shelter.wrapper.Email;
import com.clova.anifriends.domain.shelter.wrapper.IsOpenedAddress;
import com.clova.anifriends.domain.shelter.wrapper.Name;
import com.clova.anifriends.domain.shelter.wrapper.Password;
import com.clova.anifriends.domain.shelter.wrapper.PhoneNumber;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "shelter")
public class Shelter extends BaseTimeEntity {

    @Id
    @Column(name = "shelter_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shelterId;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Embedded
    private Address address;

    @Embedded
    private AddressDetail addressDetail;

    @Embedded
    private Name name;

    @Embedded
    @AttributeOverride(name = "phoneNumber", column = @Column(name = "phone_number"))
    private PhoneNumber phoneNumber;

    @Embedded
    @AttributeOverride(name = "phoneNumber", column = @Column(name = "spare_phone_number"))
    private PhoneNumber sparePhoneNumber;

    @Embedded
    private IsOpenedAddress isOpenedAddress;

    protected Shelter() {
    }

    public Shelter(
        String email,
        String password,
        String address,
        String addressDetail,
        String name,
        String phoneNumber,
        String sparePhoneNumber,
        boolean isOpenedAddress
    ) {
        this.email = new Email(email);
        this.password = new Password(password);
        this.address = new Address(address);
        this.addressDetail = new AddressDetail(addressDetail);
        this.name = new Name(name);
        this.phoneNumber = new PhoneNumber(phoneNumber);
        this.sparePhoneNumber = new PhoneNumber(sparePhoneNumber);
        this.isOpenedAddress = new IsOpenedAddress(isOpenedAddress);
    }
}
