package com.clova.anifriends.domain.volunteer;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerEmail;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerGender;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerName;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerPassword;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerPhoneNumber;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerTemperature;
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
import lombok.Getter;

@Entity
@Getter
@Table(name = "volunteer")
public class Volunteer extends BaseTimeEntity {

    @Id
    @Column(name = "volunteer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long volunteerId;

    @Embedded
    private VolunteerEmail email;

    @Embedded
    private VolunteerPassword password;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Embedded
    private VolunteerPhoneNumber phoneNumber;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private VolunteerGender gender;

    @Embedded
    private VolunteerTemperature temperature;

    @Embedded
    private VolunteerName name;

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
        this.email = new VolunteerEmail(email);
        this.password = new VolunteerPassword(password);
        this.birthDate = birthDate;
        this.phoneNumber = new VolunteerPhoneNumber(phoneNumber);
        this.gender = VolunteerGender.valueOf(gender);
        this.temperature = new VolunteerTemperature(temperature);
        this.name = new VolunteerName(name);
    }

    public String getPassword() {
        return this.password.getPassword();
    }
}
