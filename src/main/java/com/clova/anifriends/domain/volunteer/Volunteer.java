package com.clova.anifriends.domain.volunteer;

import static com.clova.anifriends.global.exception.ErrorCode.BAD_REQUEST;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
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
import java.time.format.DateTimeParseException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "volunteer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public Volunteer(
        String email,
        String password,
        String birthDate,
        String phoneNumber,
        String gender,
        String name
    ) {
        this.email = new VolunteerEmail(email);
        this.password = new VolunteerPassword(password);
        this.birthDate = validateBirthDate(birthDate);
        this.phoneNumber = new VolunteerPhoneNumber(phoneNumber);
        this.gender = VolunteerGender.from(gender);
        this.temperature = new VolunteerTemperature(36);
        this.name = new VolunteerName(name);
    }

    private LocalDate validateBirthDate(String birthDate) {
        try {
            return LocalDate.parse(birthDate);
        } catch (DateTimeParseException e) {
            throw new VolunteerBadRequestException(BAD_REQUEST, "생년월일 형식이 맞지 않습니다.");
        }
    }

    public Long getVolunteerId() {
        return volunteerId;
    }

    public String getEmail() {
        return this.email.getEmail();
    }

    public String getPassword() {
        return this.password.getPassword();
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return this.phoneNumber.getPhoneNumber();
    }

    public String getGender() {
        return this.gender.getName();
    }

    public Integer getTemperature() {
        return this.temperature.getTemperature();
    }

    public String getName() {
        return this.name.getName();
    }
}
