package com.clova.anifriends.domain.volunteer.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.VolunteerImage;
import com.clova.anifriends.domain.volunteer.dto.response.RegisterVolunteerResponse;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class VolunteerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    VolunteerService volunteerService;

    @Nested
    @DisplayName("updateVolunteerInfo 메서드 호출 시")
    class UpdateVolunteerInfoTest {

        Volunteer volunteer;
        String newName;
        VolunteerGender newGender;
        LocalDate newBirthDate;
        String newPhoneNumber;
        String newImageUrl;
        Long givenVolunteerId;

        @BeforeEach
        void setUp() {
            volunteer = VolunteerFixture.volunteer();
            RegisterVolunteerResponse registerVolunteerResponse = volunteerService.registerVolunteer(
                volunteer.getEmail(),
                volunteer.getPassword(),
                volunteer.getName(),
                volunteer.getBirthDate().toString(),
                volunteer.getPhoneNumber(),
                volunteer.getGender().toString()
            );
            givenVolunteerId = registerVolunteerResponse.volunteerId();
        }

        @Test
        @DisplayName("성공: volunteer가 업데이트 됨")
        void updateVolunteerInfo() {
            //given
            String newName = volunteer.getName() + "a";
            VolunteerGender newGender = volunteer.getGender() == VolunteerGender.FEMALE
                ? VolunteerGender.MALE : VolunteerGender.FEMALE;
            LocalDate newBirthDate = volunteer.getBirthDate().plusDays(1);
            String newPhoneNumber = "010-9999-9999";
            String newImageUrl = "asdf";

            //when
            volunteerService.updateVolunteerInfo(givenVolunteerId, newName, newGender,
                newBirthDate, newPhoneNumber, newImageUrl);

            //then
            Volunteer updatedVolunteer
                = entityManager.find(Volunteer.class, givenVolunteerId);
            assertThat(updatedVolunteer.getName()).isEqualTo(newName);
            assertThat(updatedVolunteer.getGender()).isEqualTo(newGender);
            assertThat(updatedVolunteer.getBirthDate()).isEqualTo(newBirthDate);
            assertThat(updatedVolunteer.getPhoneNumber()).isEqualTo(newPhoneNumber);
            assertThat(updatedVolunteer.getVolunteerImageUrl()).isEqualTo(newImageUrl);
        }

        @Test
        @DisplayName("성공: 이미지 url 입력값이 들어왔고, 봉사자 이미지와 같다면 새롭게 생성된 volunteerImage 엔티티는 없다.")
        void updateVolunteerInfoButNoMoreVolunteerImageEntity() {
            //given
            String equalsImageUrl = "asdf";
            volunteerService.updateVolunteerInfo(givenVolunteerId, null, null, null, null,
                equalsImageUrl);

            //when
            volunteerService.updateVolunteerInfo(givenVolunteerId, null, null, null, null,
                equalsImageUrl);

            //then
            List<VolunteerImage> result = entityManager.createQuery(
                    "select vi from VolunteerImage vi", VolunteerImage.class)
                .getResultList();
            assertThat(result).hasSize(1);
        }
    }
}
