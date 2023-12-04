package com.clova.anifriends.domain.volunteer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.auth.support.MockPasswordEncoder;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import java.time.LocalDate;
import java.util.Optional;
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
            volunteer.updateVolunteerInfo(newName, newGender, newBirthDate, newPhoneNumber,
                newImageUrl);

            //then
            assertThat(volunteer.getName()).isEqualTo(newName);
            assertThat(volunteer.getGender()).isEqualTo(newGender);
            assertThat(volunteer.getBirthDate()).isEqualTo(newBirthDate);
            assertThat(volunteer.getPhoneNumber()).isEqualTo(newPhoneNumber);
            assertThat(volunteer.getVolunteerImageUrl()).isEqualTo(newImageUrl);
        }

        @Test
        @DisplayName("성공: 이미지 url 입력값이 null이고, 봉사자 이미지가 null인 경우 현재 이미지는 공백이다.")
        void updateVolunteerNullImage() {
            //given
            String nullImageUrl = null;

            //when
            volunteer.updateVolunteerInfo(volunteer.getName(), volunteer.getGender(),
                volunteer.getBirthDate(), volunteer.getPhoneNumber(), nullImageUrl);

            //then
            assertThat(volunteer.getVolunteerImageUrl()).isBlank();
        }

        @Test
        @DisplayName("성공: 이미지 url 입력값이 들어왔고, 봉사자 이미지가 null인 경우 현재 이미지는 입력값이다.")
        void updateVolunteerNewImage() {
            //given
            String newImageUrl = "asdf";

            //when
            volunteer.updateVolunteerInfo(volunteer.getName(), volunteer.getGender(),
                volunteer.getBirthDate(), volunteer.getPhoneNumber(), newImageUrl);

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
                equalsImageUrl);
            Object beforeVolunteerImage = ReflectionTestUtils.getField(volunteer, "image");

            //when
            volunteer.updateVolunteerInfo(
                volunteer.getName(), volunteer.getGender(), volunteer.getBirthDate(),
                volunteer.getPhoneNumber(), equalsImageUrl);

            //then
            Object afterVolunteerImage = ReflectionTestUtils.getField(volunteer, "image");
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
                imageUrl);
            String notEqualsImageUrl = imageUrl + "a";

            //when
            volunteer.updateVolunteerInfo(
                volunteer.getName(), volunteer.getGender(), volunteer.getBirthDate(),
                volunteer.getPhoneNumber(), notEqualsImageUrl);

            //then
            assertThat(volunteer.getVolunteerImageUrl()).isEqualTo(notEqualsImageUrl);
        }

        @Test
        @DisplayName("성공: 이미지 url 입력값이 공백이고, 봉사자 이미지가 존재한다면 현재 이미지는 공백이다.")
        void updateVolunteerWhenImageUrlIsBlank() {
            //given
            String imageUrl = "asdf";
            volunteer.updateVolunteerInfo(volunteer.getName(),
                volunteer.getGender(), volunteer.getBirthDate(), volunteer.getPhoneNumber(),
                imageUrl);
            String blankImageUrl = "";

            //when
            volunteer.updateVolunteerInfo(
                volunteer.getName(), volunteer.getGender(), volunteer.getBirthDate(),
                volunteer.getPhoneNumber(), blankImageUrl);

            //then
            assertThat(volunteer.getVolunteerImageUrl()).isBlank();
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

    @Nested
    @DisplayName("findDeletedImageUrl 실행 시")
    class FindImageToDelete {

        @Test
        @DisplayName("성공: 기존의 이미지가 존재하고 새로운 이미지와 다를 경우 기존의 이미지를 반환")
        void findImageToDeleteWhenDifferentFromNow() {
            // given
            String originImageUrl = "originImageUrl";
            String newImageUrl = "newImageUrl";

            Volunteer volunteer = VolunteerFixture.volunteer(originImageUrl);

            // when
            Optional<String> result = volunteer.findImageToDelete(newImageUrl);

            // then
            assertThat(result).isEqualTo(Optional.of(originImageUrl));
        }

        @Test
        @DisplayName("성공: 기존의 이미지가 존재하고 새로운 이미지와 같을 경우 null반환")
        void findImageToDeleteWhenSameWithNow() {
            // given
            String sameImageUrl = "sameImageUrl";

            Volunteer volunteer = VolunteerFixture.volunteer(sameImageUrl);

            // when
            Optional<String> result = volunteer.findImageToDelete(sameImageUrl);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("성공: 기존의 이미지가 존재하지 않으면 null반환")
        void findImageToDeleteWhenNowIsNull() {
            // given
            String newImageUrl = "newImageUrl";
            Volunteer volunteer = VolunteerFixture.volunteer();

            // when
            Optional<String> result = volunteer.findImageToDelete(newImageUrl);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("updateDeviceToken 실행 시")
    class UpdateDeviceTokenTest {

        @Test
        @DisplayName("성공")
        void updateDeviceToken() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            String updateToken = "update";

            // when
            volunteer.updateDeviceToken(updateToken);

            // then
            assertThat(volunteer.getDeviceToken()).isEqualTo(updateToken);
        }

        @Test
        @DisplayName("예외(VolunteerBadRequestException): 토큰이 null인 경우")
        void throwExceptionWhenTokenIsNull() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            String updateToken = null;

            // when
            Exception exception = catchException(() -> volunteer.updateDeviceToken(updateToken));

            // then
            assertThat(exception).isInstanceOf(VolunteerBadRequestException.class);
        }
    }
}
