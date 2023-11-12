package com.clova.anifriends.domain.volunteer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.base.MockImageRemover;
import com.clova.anifriends.domain.auth.support.MockPasswordEncoder;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
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

    CustomPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new MockPasswordEncoder();
    }

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
                name, passwordEncoder);

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
                name, passwordEncoder))
                .isInstanceOf(VolunteerBadRequestException.class);
        }
    }

    @Nested
    @DisplayName("volunteerUpdate 메서드 호출 시")
    class UpdateVolunteerTest {

        Volunteer volunteer;
        ImageRemover imageRemover;

        @BeforeEach
        void setUp() {
            volunteer = VolunteerFixture.volunteer();
            imageRemover = new MockImageRemover();
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
            volunteer.updateVolunteerInfo(newName, newGender,
                newBirthDate, newPhoneNumber, newImageUrl, imageRemover);

            //then
            assertThat(volunteer.getName()).isEqualTo(newName);
            assertThat(volunteer.getGender()).isEqualTo(newGender);
            assertThat(volunteer.getBirthDate()).isEqualTo(newBirthDate);
            assertThat(volunteer.getPhoneNumber()).isEqualTo(newPhoneNumber);
            assertThat(volunteer.getVolunteerImageUrl()).isEqualTo(newImageUrl);
        }

        @Test
        @DisplayName("성공: 이미지 url 입력값이 null이고, 봉사자 이미지가 null인 경우 현재 이미지는 null이다.")
        void updateVolunteerNullImage() {
            //given
            String nullImageUrl = null;

            //when
            volunteer.updateVolunteerInfo(volunteer.getName(), volunteer.getGender(),
                volunteer.getBirthDate(), volunteer.getPhoneNumber(), nullImageUrl, imageRemover);

            //then
            assertThat(volunteer.getVolunteerImageUrl()).isNull();
        }

        @Test
        @DisplayName("성공: 이미지 url 입력값이 들어왔고, 봉사자 이미지가 null인 경우 현재 이미지는 입력값이다.")
        void updateVolunteerNewImage() {
            //given
            String newImageUrl = "asdf";

            //when
            volunteer.updateVolunteerInfo(volunteer.getName(), volunteer.getGender(),
                volunteer.getBirthDate(), volunteer.getPhoneNumber(), newImageUrl, imageRemover);

            //then
            assertThat(volunteer.getVolunteerImageUrl()).isEqualTo(newImageUrl);
        }

        @Test
        @DisplayName("성공: 이미지 url 입력값이 들어왔고, 봉사자 이미지와 같다면 현재 이미지는 업데이트 되지 않는다.")
        void updateVolunteerImageWhenEqualsImageUrl() {
            //given
            String equalsImageUrl = "asdf";
            volunteer.updateVolunteerInfo(volunteer.getName(),
                volunteer.getGender(), volunteer.getBirthDate(), volunteer.getPhoneNumber(),
                equalsImageUrl, imageRemover);
            Object beforeVolunteerImage = ReflectionTestUtils.getField(volunteer, "volunteerImage");

            //when
            volunteer.updateVolunteerInfo(
                volunteer.getName(), volunteer.getGender(), volunteer.getBirthDate(),
                volunteer.getPhoneNumber(), equalsImageUrl, imageRemover);

            //then
            Object afterVolunteerImage = ReflectionTestUtils.getField(volunteer, "volunteerImage");
            assertThat(volunteer.getVolunteerImageUrl()).isEqualTo(equalsImageUrl);
            assertThat(beforeVolunteerImage).isEqualTo(afterVolunteerImage);
        }

        @Test
        @DisplayName("성공: 이미지 url 입력값이 들어왔고, 봉사자 이미지와 다르다면 현재 이미지는 입력값이다.")
        void updateVolunteerImageWhenNotEqualsImageUrl() {
            //given
            String imageUrl = "asdf";
            volunteer.updateVolunteerInfo(volunteer.getName(),
                volunteer.getGender(), volunteer.getBirthDate(), volunteer.getPhoneNumber(),
                imageUrl, imageRemover);
            String notEqualsImageUrl = imageUrl + "a";

            //when
            volunteer.updateVolunteerInfo(
                volunteer.getName(), volunteer.getGender(), volunteer.getBirthDate(),
                volunteer.getPhoneNumber(), notEqualsImageUrl, imageRemover);

            //then
            assertThat(volunteer.getVolunteerImageUrl()).isEqualTo(notEqualsImageUrl);
        }
    }

    @Nested
    @DisplayName("updatePassword 메서드 호출 시")
    class updatePasswordTest {

        @Test
        @DisplayName("성공")
        void updatePassword() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            String rawOldPassword = VolunteerFixture.PASSWORD;
            String rawNewPassword = rawOldPassword + "a";

            // when
            volunteer.updatePassword(passwordEncoder, rawOldPassword, rawNewPassword);

            // then
            String encodedUpdatePassword = volunteer.getPassword();
            boolean match = passwordEncoder.matchesPassword(encodedUpdatePassword, rawNewPassword);

            assertThat(match).isTrue();
        }

        @Test
        @DisplayName("예외(VolunteerBadRequest) : 이전 비밀번호와 새로운 비밀번호가 같은 경우")
        void throwExceptionWhenOldPasswordEqualsNewPassword() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            String rawOldPassword = VolunteerFixture.PASSWORD;
            String rawNewPassword = rawOldPassword;

            // when
            Exception exception = catchException(
                () -> volunteer.updatePassword(passwordEncoder, rawOldPassword, rawNewPassword));

            // then
            assertThat(exception).isInstanceOf(VolunteerBadRequestException.class);
        }

        @Test
        @DisplayName("예외(VolunteerBadRequest) : 입력한 이전 비밀번호가 다른 경우")
        void throwExceptionWhenOldPasswordIsNotSame() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            String rawOldPassword = VolunteerFixture.PASSWORD + "123";
            String rawNewPassword = rawOldPassword + "1";

            // when
            Exception exception = catchException(
                () -> volunteer.updatePassword(passwordEncoder, rawOldPassword, rawNewPassword));

            // then
            assertThat(exception).isInstanceOf(VolunteerBadRequestException.class);
        }
    }
}
