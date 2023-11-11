package com.clova.anifriends.domain.volunteer;

import static com.clova.anifriends.global.exception.ErrorCode.BAD_REQUEST;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.common.ImageRemover;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerEmail;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerGender;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerName;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerPassword;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerPhoneNumber;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerTemperature;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    @OneToMany(mappedBy = "volunteer", fetch = FetchType.LAZY)
    private List<Applicant> applicants = new ArrayList<>();

    @OneToOne(mappedBy = "volunteer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private VolunteerImage volunteerImage;

    public Volunteer(
        String email,
        String password,
        String birthDate,
        String phoneNumber,
        String gender,
        String name,
        CustomPasswordEncoder passwordEncoder
    ) {
        this.email = new VolunteerEmail(email);
        this.password = new VolunteerPassword(password, passwordEncoder);
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

    public void addApplicant(Applicant applicant) {
        applicants.add(applicant);
    }

    public void updateVolunteerImage(VolunteerImage volunteerImage) {
        this.volunteerImage = volunteerImage;
    }

    public void updateVolunteerInfo(
        String name,
        VolunteerGender gender,
        LocalDate birthDate,
        String phoneNumber,
        String imageUrl,
        ImageRemover imageRemover) {
        this.name = this.name.updateName(name);
        this.gender = updateGender(gender);
        this.birthDate = updateBirthDate(birthDate);
        this.phoneNumber = this.phoneNumber.updatePhoneNumber(phoneNumber);
        this.volunteerImage = updateVolunteerImage(imageUrl, imageRemover);
    }

    private LocalDate updateBirthDate(LocalDate birthDate) {
        return birthDate != null ? birthDate : this.birthDate;
    }

    private VolunteerGender updateGender(VolunteerGender gender) {
        return gender != null ? gender : this.gender;
    }

    private VolunteerImage updateVolunteerImage(String imageUrl, ImageRemover imageRemover) {
        if (Objects.nonNull(volunteerImage) && volunteerImage.isEqualImageUrl(imageUrl)) {
            return this.volunteerImage;
        }
        clearVolunteerImageIfExists(imageRemover);
        if(Objects.isNull(imageUrl)) {
            return null;
        }
        return new VolunteerImage(this, imageUrl);
    }

    private void clearVolunteerImageIfExists(ImageRemover imageRemover) {
        if (Objects.nonNull(volunteerImage)) {
            volunteerImage.removeImage(imageRemover);
        }
    }

    public long getReviewCount() {
        return applicants.stream()
            .filter(applicant -> Objects.nonNull(applicant.getReview()))
            .count();
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

    public VolunteerGender getGender() {
        return this.gender;
    }

    public Integer getTemperature() {
        return this.temperature.getTemperature();
    }

    public String getName() {
        return this.name.getName();
    }

    public String getVolunteerImageUrl() {
        return this.volunteerImage == null ? null : volunteerImage.getImageUrl();
    }

    public List<Applicant> getApplicants() {
        return Collections.unmodifiableList(applicants);
    }

    public Integer getApplicantCompletedCount() {
        return Math.toIntExact(applicants.stream()
            .filter(Applicant::isCompleted)
            .count());
    }
}
