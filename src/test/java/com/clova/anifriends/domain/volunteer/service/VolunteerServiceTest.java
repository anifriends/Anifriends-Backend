package com.clova.anifriends.domain.volunteer.service;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.clova.anifriends.domain.auth.support.MockPasswordEncoder;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.common.event.ImageDeletionEvent;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.VolunteerImage;
import com.clova.anifriends.domain.volunteer.dto.request.RegisterVolunteerRequest;
import com.clova.anifriends.domain.volunteer.dto.response.CheckDuplicateVolunteerEmailResponse;
import com.clova.anifriends.domain.volunteer.dto.response.FindVolunteerMyPageResponse;
import com.clova.anifriends.domain.volunteer.dto.response.FindVolunteerProfileResponse;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.domain.volunteer.support.VolunteerDtoFixture;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import com.clova.anifriends.domain.volunteer.support.VolunteerImageFixture;
import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class VolunteerServiceTest {

    @InjectMocks
    VolunteerService volunteerService;

    @Mock
    VolunteerRepository volunteerRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Spy
    CustomPasswordEncoder passwordEncoder = new MockPasswordEncoder();

    @Nested
    @DisplayName("checkDuplicateVolunteerEmail 메서드 실행 시")
    class CheckDuplicateVolunteerEmailTest {

        @Test
        @DisplayName("성공")
        void checkDuplicateVolunteerEmail() {
            //given
            String email = "email@email.com";

            given(volunteerRepository.existsByEmail(any())).willReturn(true);

            //when
            CheckDuplicateVolunteerEmailResponse response
                = volunteerService.checkDuplicateVolunteerEmail(email);

            //then
            assertThat(response.isDuplicated()).isTrue();
        }
    }

    @Nested
    @DisplayName("registerVolunteer 메서드 실행 시")
    class RegisterVolunteerTest {

        Volunteer volunteer = VolunteerFixture.volunteer();

        @Test
        @DisplayName("성공")
        void success() {
            // given
            given(volunteerRepository.save(any())).willReturn(volunteer);
            RegisterVolunteerRequest request = VolunteerDtoFixture.registerVolunteerRequest();

            // when
            volunteerService.registerVolunteer(
                request.email(),
                request.password(),
                request.name(),
                request.birthDate(),
                request.phoneNumber(),
                request.gender()
            );

            // then
            then(volunteerRepository).should().save(any());
        }
    }

    @Nested
    @DisplayName("findVolunteerMyPage 메서드 실행 시")
    class FindVolunteerMyPageTest {

        Volunteer volunteer;
        VolunteerImage volunteerImage;

        @Test
        @DisplayName("성공")
        void success() {
            // given
            volunteer = VolunteerFixture.volunteer();
            ReflectionTestUtils.setField(volunteer, "volunteerId", 1L);
            volunteerImage = VolunteerImageFixture.volunteerImage(volunteer);
            setField(volunteer, "image", volunteerImage);
            FindVolunteerMyPageResponse expected = FindVolunteerMyPageResponse.from(volunteer);

            given(volunteerRepository.findById(anyLong())).willReturn(ofNullable(volunteer));

            // when
            FindVolunteerMyPageResponse result = volunteerService.findVolunteerMyPage(1L);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("findVolunteerProfile 메서드 실행 시")
    class FindVolunteerProfileTest {

        Volunteer volunteer;
        VolunteerImage volunteerImage;

        @Test
        @DisplayName("성공")
        void findVolunteerProfile() {
            // given
            volunteer = VolunteerFixture.volunteer();
            volunteerImage = VolunteerImageFixture.volunteerImage(volunteer);
            setField(volunteer, "image", volunteerImage);
            FindVolunteerProfileResponse expectedFindVolunteerProfileResponse = FindVolunteerProfileResponse.from(
                volunteer);

            given(volunteerRepository.findById(anyLong())).willReturn(ofNullable(volunteer));

            // when
            FindVolunteerProfileResponse foundFindVolunteerProfileResponse = volunteerService.findVolunteerProfile(
                1L);

            // then
            assertThat(foundFindVolunteerProfileResponse).usingRecursiveComparison()
                .isEqualTo(expectedFindVolunteerProfileResponse);
        }
    }

    @Nested
    @DisplayName("updatedVolunteerInfo 메서드 호출 시")
    class UpdateVolunteerInfoTest {

        @Test
        @DisplayName("성공")
        void updateVolunteerInfo() {
            //given
            Volunteer volunteer = VolunteerFixture.volunteer();
            String newName = volunteer.getName() + "a";
            VolunteerGender newGender = volunteer.getGender() == VolunteerGender.FEMALE
                ? VolunteerGender.MALE : VolunteerGender.FEMALE;
            LocalDate newBirthDate = volunteer.getBirthDate().plusDays(1);
            String newPhoneNumber = "010-9999-9999";
            String newImageUrl = "asdf";

            given(volunteerRepository.findById(anyLong())).willReturn(ofNullable(volunteer));

            //when
            volunteerService.updateVolunteerInfo(1L, newName, newGender, newBirthDate,
                newPhoneNumber, newImageUrl);

            //then
            verify(applicationEventPublisher, times(0)).publishEvent(any());
            assertThat(volunteer.getName()).isEqualTo(newName);
            assertThat(volunteer.getGender()).isEqualTo(newGender);
            assertThat(volunteer.getBirthDate()).isEqualTo(newBirthDate);
            assertThat(volunteer.getPhoneNumber()).isEqualTo(newPhoneNumber);
            assertThat(volunteer.getVolunteerImageUrl()).isEqualTo(newImageUrl);
        }

        @Test
        @DisplayName("성공: 기존 이미지 존재 -> 새로운 이미지 갱신")
        void updateWhenExistToNewImage() {
            // given
            String originImageUrl = "originImageUrl";
            String newImageUrl = "newImageUrl";
            Volunteer volunteer = VolunteerFixture.volunteer(originImageUrl);

            when(volunteerRepository.findById(anyLong())).thenReturn(Optional.of(volunteer));

            // when
            Exception exception = catchException(() -> volunteerService.updateVolunteerInfo(
                anyLong(), volunteer.getName(), volunteer.getGender(), volunteer.getBirthDate(),
                volunteer.getPhoneNumber(), newImageUrl));

            // then
            verify(applicationEventPublisher, times(1)).publishEvent(
                new ImageDeletionEvent(List.of(originImageUrl)));
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공: 기존 이미지 존재 -> 동일한 이미지")
        void updateWhenSameImage() {
            // given
            String sameImageUrl = "originImageUrl";
            Volunteer volunteer = VolunteerFixture.volunteer(sameImageUrl);

            when(volunteerRepository.findById(anyLong())).thenReturn(Optional.of(volunteer));

            // when
            Exception exception = catchException(() -> volunteerService.updateVolunteerInfo(
                anyLong(), volunteer.getName(), volunteer.getGender(), volunteer.getBirthDate(),
                volunteer.getPhoneNumber(), sameImageUrl));

            // then
            verify(applicationEventPublisher, times(0)).publishEvent(any());
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공: 기존 이미지 존재 -> 이미지 none")
        void updateWhenExistToNoneImage() {
            // given
            String originImageUrl = "originImageUrl";
            String nullNewImageUrl = null;
            Volunteer volunteer = VolunteerFixture.volunteer(originImageUrl);

            when(volunteerRepository.findById(anyLong())).thenReturn(Optional.of(volunteer));

            // when
            Exception exception = catchException(() -> volunteerService.updateVolunteerInfo(
                anyLong(), volunteer.getName(), volunteer.getGender(), volunteer.getBirthDate(),
                volunteer.getPhoneNumber(), nullNewImageUrl));

            // then
            verify(applicationEventPublisher, times(1)).publishEvent(
                new ImageDeletionEvent(List.of(originImageUrl)));
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공: 이미지 none -> 새로운 이미지 갱신")
        void updateWhenNoneToNewImage() {
            // given
            String nullOriginImageUrl = null;
            String newImageUrl = "newImageUrl";
            Volunteer volunteer = VolunteerFixture.volunteer(nullOriginImageUrl);

            when(volunteerRepository.findById(anyLong())).thenReturn(Optional.of(volunteer));

            // when
            Exception exception = catchException(() -> volunteerService.updateVolunteerInfo(
                anyLong(), volunteer.getName(), volunteer.getGender(), volunteer.getBirthDate(),
                volunteer.getPhoneNumber(), newImageUrl));

            // then
            verify(applicationEventPublisher, times(0)).publishEvent(any());
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공: 이미지 none -> 이미지 none")
        void updateWhenNoneToNoneImage() {
            // given
            String nullImageUrl = null;
            Volunteer volunteer = VolunteerFixture.volunteer(nullImageUrl);

            when(volunteerRepository.findById(anyLong())).thenReturn(Optional.of(volunteer));

            // when
            Exception exception = catchException(() -> volunteerService.updateVolunteerInfo(
                anyLong(), volunteer.getName(), volunteer.getGender(), volunteer.getBirthDate(),
                volunteer.getPhoneNumber(), nullImageUrl));

            // then
            verify(applicationEventPublisher, times(0)).publishEvent(any());
            assertThat(exception).isNull();
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

            given(volunteerRepository.findById(anyLong())).willReturn(Optional.of(volunteer));

            // when
            volunteerService.updatePassword(1L, rawOldPassword, rawNewPassword);

            // then
            String encodedUpdatePassword = volunteer.getPassword();
            boolean match = passwordEncoder.matchesPassword(encodedUpdatePassword, rawNewPassword);

            assertThat(match).isTrue();
        }
    }
}
