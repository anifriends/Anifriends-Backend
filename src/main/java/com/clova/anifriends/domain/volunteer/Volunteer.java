package com.clova.anifriends.domain.volunteer;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.volunteer.wrapper.Email;
import com.clova.anifriends.domain.volunteer.wrapper.Gender;
import com.clova.anifriends.domain.volunteer.wrapper.Name;
import com.clova.anifriends.domain.volunteer.wrapper.Password;
import com.clova.anifriends.domain.volunteer.wrapper.PhoneNumber;
import com.clova.anifriends.domain.volunteer.wrapper.Temperature;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "volunteer")
public class Volunteer extends BaseTimeEntity {

    @Id
    @Column(name = "volunteer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long volunteerId;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Embedded
    private PhoneNumber phoneNumber;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded
    private Temperature temperature;

    @Embedded
    private Name name;

    protected Volunteer() {
    }

    public Volunteer(
        String email,
        String password,
        LocalDate birthDate,
        String phoneNumber,
        String gender,
        int temperature,
        String name
    ) {
        this.email = new Email(email);
        this.password = new Password(password);
        this.birthDate = birthDate;
        this.phoneNumber = new PhoneNumber(phoneNumber);
        this.gender = Gender.valueOf(gender);
        this.temperature = new Temperature(temperature);
        this.name = new Name(name);
    }
}
