package com.clova.anifriends.domain.volunteer.service;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.clova.anifriends.domain.auth.support.MockPasswordEncoder;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
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
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerGender;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class VolunteerServiceTest {

    @InjectMocks
    VolunteerService volunteerService;

    @Mock
    VolunteerRepository volunteerRepository;

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
            assertThat(volunteer.getName()).isEqualTo(newName);
            assertThat(volunteer.getGender()).isEqualTo(newGender);
            assertThat(volunteer.getBirthDate()).isEqualTo(newBirthDate);
            assertThat(volunteer.getPhoneNumber()).isEqualTo(newPhoneNumber);
            assertThat(volunteer.getVolunteerImageUrl()).isEqualTo(newImageUrl);
        }
    }
}
