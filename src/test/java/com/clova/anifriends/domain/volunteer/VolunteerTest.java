package com.clova.anifriends.domain.volunteer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clova.anifriends.base.MockImageRemover;
import com.clova.anifriends.domain.common.ImageRemover;
import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerGender;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class VolunteerTest {

    @Nested
    @DisplayName("Volunteer 생성 시")
    class newVolunteerTest {

        String email = "asdf@gmail.com";
        String password = "asdf1234";
        String birthDate;
        String phoneNumber = "010-1234-1234";
        String gender = "MALE";
        String name = "홍길동";

        @Test
        @DisplayName("성공")
        void success() {
            // given
            birthDate = "1990-01-01";

            // when
            Volunteer volunteer = new Volunteer(email, password, birthDate, phoneNumber, gender,
                name);

            // then
            assertThat(volunteer.getEmail()).isEqualTo(email);
        }

        @Test
        @DisplayName("예외: 생년월일 형식이 잘못된 경우")
        void throwExceptionWhenBirthDateFormatIsWrong() {
            // given
            birthDate = "1990:01:12";

            // when
            // then
            assertThatThrownBy(() -> new Volunteer(email, password, birthDate, phoneNumber, gender,
                name))
                .isInstanceOf(VolunteerBadRequestException.class);
        }
    }

    @Nested
    @DisplayName("volunteerUpdate 메서드 호출 시")
    class UpdateVolunteerTest {

        Volunteer volunteer;
        ImageRemover imageRemover = new MockImageRemover();

        @BeforeEach
        void setUp() {
            volunteer = VolunteerFixture.volunteer();
        }

        @Test
        @DisplayName("성공: 봉사자의 상태가 변경된다.")
        void updateVolunteer() {
            //given
            String newName = volunteer.getName() + "a";
            VolunteerGender newGender = VolunteerGender.FEMALE;
            LocalDate newBirthDate = volunteer.getBirthDate().plusDays(1);
            String newPhoneNumber = "010-1234-1111";
            String newImageUrl = volunteer.getVolunteerImageUrl() + "1";

            //when
            Volunteer updatedVolunteer = volunteer.updateVolunteerInfo(newName, newGender,
                newBirthDate, newPhoneNumber, newImageUrl, imageRemover);

            //then
            assertThat(updatedVolunteer.getName()).isEqualTo(newName);
            assertThat(updatedVolunteer.getGender()).isEqualTo(newGender);
            assertThat(updatedVolunteer.getBirthDate()).isEqualTo(newBirthDate);
            assertThat(updatedVolunteer.getPhoneNumber()).isEqualTo(newPhoneNumber);
            assertThat(updatedVolunteer.getVolunteerImageUrl()).isEqualTo(newImageUrl);
        }

        @Test
        @DisplayName("성공: 이미지 url 입력값이 null이고, 봉사자 이미지가 null인 경우 현재 이미지는 null이다.")
        void updateVolunteerNullImage() {
            //given
            String nullImageUrl = null;

            //when
            Volunteer updatedVolunteer = volunteer.updateVolunteerInfo(volunteer.getName(),
                volunteer.getGender(), volunteer.getBirthDate(), volunteer.getPhoneNumber(),
                nullImageUrl, imageRemover);

            //then
            assertThat(updatedVolunteer.getVolunteerImageUrl()).isNull();
        }

        @Test
        @DisplayName("성공: 이미지 url 입력값이 들어왔고, 봉사자 이미지가 null인 경우 현재 이미지는 입력값이다.")
        void updateVolunteerNewImage() {
            //given
            String newImageUrl = "asdf";

            //when
            Volunteer updatedVolunteer = volunteer.updateVolunteerInfo(volunteer.getName(),
                volunteer.getGender(), volunteer.getBirthDate(), volunteer.getPhoneNumber(),
                newImageUrl, imageRemover);

            //then
            assertThat(updatedVolunteer.getVolunteerImageUrl()).isEqualTo(newImageUrl);
            VolunteerImage volunteerImage = (VolunteerImage) ReflectionTestUtils.getField(
                updatedVolunteer, "volunteerImage");
            assertThat(volunteerImage.getVolunteer()).isEqualTo(updatedVolunteer);
        }

        @Test
        @DisplayName("성공: 이미지 url 입력값이 들어왔고, 봉사자 이미지와 같다면 현재 이미지는 업데이트 된 봉사자를 참조한다.")
        void updateVolunteerImageWhenEqualsImageUrl() {
            //given
            String equalsImageUrl = "asdf";
            Volunteer updatedVolunteer = volunteer.updateVolunteerInfo(volunteer.getName(),
                volunteer.getGender(), volunteer.getBirthDate(), volunteer.getPhoneNumber(),
                equalsImageUrl, imageRemover);

            //when
            Volunteer secondUpdatedVolunteer = updatedVolunteer.updateVolunteerInfo(
                volunteer.getName(), volunteer.getGender(), volunteer.getBirthDate(),
                volunteer.getPhoneNumber(), equalsImageUrl, imageRemover);

            //then
            assertThat(secondUpdatedVolunteer.getVolunteerImageUrl())
                .isEqualTo(updatedVolunteer.getVolunteerImageUrl());
            VolunteerImage secondUpdatedImage = (VolunteerImage) ReflectionTestUtils.getField(
                secondUpdatedVolunteer,
                "volunteerImage");
            assertThat(secondUpdatedImage.getVolunteer()).isEqualTo(secondUpdatedVolunteer);
        }

        @Test
        @DisplayName("성공: 이미지 url 입력값이 들어왔고, 봉사자 이미지와 다르다면 현재 이미지는 입력값이다.")
        void updateVolunteerImageWhenNotEqualsImageUrl() {
            //given
            String imageUrl = "asdf";
            Volunteer updatedVolunteer = volunteer.updateVolunteerInfo(volunteer.getName(),
                volunteer.getGender(), volunteer.getBirthDate(), volunteer.getPhoneNumber(),
                imageUrl, imageRemover);
            String notEqualsImageUrl = imageUrl + "a";

            //when
            Volunteer secondUpdatedVolunteer = updatedVolunteer.updateVolunteerInfo(
                volunteer.getName(), volunteer.getGender(), volunteer.getBirthDate(),
                volunteer.getPhoneNumber(), notEqualsImageUrl, imageRemover);

            //then
            assertThat(secondUpdatedVolunteer.getVolunteerImageUrl()).isEqualTo(notEqualsImageUrl);
        }
    }
}