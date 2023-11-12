package com.clova.anifriends.domain.shelter.service;

import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

import com.clova.anifriends.domain.auth.support.MockPasswordEncoder;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.ShelterImage;
import com.clova.anifriends.domain.shelter.dto.CheckDuplicateShelterResponse;
import com.clova.anifriends.domain.shelter.dto.FindShelterDetailResponse;
import com.clova.anifriends.domain.shelter.dto.FindShelterMyPageResponse;
import com.clova.anifriends.domain.shelter.dto.FindShelterSimpleResponse;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.shelter.support.ShelterImageFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShelterServiceTest {

    @InjectMocks
    private ShelterService shelterService;

    @Mock
    private ShelterRepository shelterRepository;

    @Spy
    CustomPasswordEncoder passwordEncoder = new MockPasswordEncoder();

    Shelter givenShelter;
    ShelterImage givenShelterImage;

    @Nested
    @DisplayName("checkDuplicateEmail 실행 시")
    class CheckDuplicateEmailTest {

        @Test
        @DisplayName("성공")
        void checkDuplicateEmail() {
            //given
            String email = "email@email.com";

            given(shelterRepository.existsByEmail(any())).willReturn(true);

            //when
            CheckDuplicateShelterResponse response
                = shelterService.checkDuplicateEmail(email);

            //then
            assertThat(response.isDuplicated()).isTrue();
        }

    }

    @Nested
    @DisplayName("registerShelter 실행 시")
    class RegisterShelterTest {

        String email = "email@email.com";
        String password = "12345678";
        String name = "보호소";
        String address = "주소1";
        String addressDetail = "주소2";
        String phoneNumber = "010-1234-5678";
        String sparePhoneNumber = "010-1234-5678";
        boolean isOpenedAddress = true;

        @Test
        @DisplayName("성공")
        void registerShelter() {
            //given
            //when
            shelterService.registerShelter(email, password, name, address, addressDetail,
                phoneNumber, sparePhoneNumber, isOpenedAddress);

            //then
            then(shelterRepository).should().save(any());
        }
    }

    @Nested
    @DisplayName("findShelterDetail 실행 시")
    class FindShelterDetailTest {

        @Test
        @DisplayName("성공")
        void findShelterDetail() {
            //given
            givenShelter = ShelterFixture.shelter();

            given(shelterRepository.findById(anyLong())).willReturn(
                Optional.ofNullable(givenShelter));
            FindShelterDetailResponse expectedFindShelterDetailResponse = FindShelterDetailResponse.from(
                givenShelter);

            //when
            FindShelterDetailResponse foundShelterDetailResponse = shelterService.findShelterDetail(
                anyLong());

            //then
            assertThat(foundShelterDetailResponse).usingRecursiveComparison()
                .ignoringFields("shelterId")
                .isEqualTo(expectedFindShelterDetailResponse);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 존재하지 않은 보호소")
        void throwExceptionWhenShelterNotFound() {
            //given
            Long shelterId = 1L;

            given(shelterRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> shelterService.findShelterDetail(shelterId));

            // then
            assertThat(exception).isInstanceOf(ShelterNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findShelterMyPage 실행 시")
    class FindShelterMyPageTest {

        @Test
        @DisplayName("성공")
        void findShelterMyPage() {
            //given
            givenShelter = ShelterFixture.shelter();
            givenShelterImage = ShelterImageFixture.shelterImage(givenShelter);
            givenShelter.updateShelterImage(givenShelterImage);

            given(shelterRepository.findById(anyLong())).willReturn(
                Optional.ofNullable(givenShelter));
            FindShelterMyPageResponse expectedFindShelterMyPageResponse = FindShelterMyPageResponse.from(
                givenShelter);

            //when
            FindShelterMyPageResponse foundShelterMyPageResponse = shelterService.findShelterMyPage(
                anyLong());

            //then
            assertThat(foundShelterMyPageResponse).usingRecursiveComparison()
                .ignoringFields("shelterId")
                .isEqualTo(expectedFindShelterMyPageResponse);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 존재하지 않은 보호소")
        void throwExceptionWhenShelterNotFound() {
            //given
            Long shelterId = 1L;

            given(shelterRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> shelterService.findShelterMyPage(shelterId));

            // then
            assertThat(exception).isInstanceOf(ShelterNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findShelterSimple 실행 시")
    class FindShelterSimpleTest {

        @Test
        @DisplayName("성공")
        void findShelterSimple() {
            // given
            Shelter shelter = shelter();
            FindShelterSimpleResponse expected = FindShelterSimpleResponse.from(
                shelter);

            given(shelterRepository.findById(anyLong())).willReturn(Optional.of(shelter));

            // when
            FindShelterSimpleResponse foundShelterByVolunteerReview = shelterService.findShelterSimple(
                anyLong());

            // then
            assertThat(foundShelterByVolunteerReview).usingRecursiveComparison()
                .ignoringFields("recruitmentId")
                .isEqualTo(expected);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 존재하지 않는 모집글")
        void throwExceptionWhenShelterIsNotExist() {
            // given
            when(shelterRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> shelterService.findShelterSimple(anyLong()));

            // then
            assertThat(exception).isInstanceOf(ShelterNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("updatePassword 메서드 호출 시")
    class UpdatePasswordTest {

        @Test
        @DisplayName("성공")
        void updatePassword() {
            //given
            Shelter shelter = shelter();
            String rawOldPassword = ShelterFixture.RAW_SHELTER_PASSWORD;
            String rawNewPassword = rawOldPassword + "a";

            given(shelterRepository.findById(anyLong())).willReturn(Optional.of(shelter));

            //when
            shelterService.updatePassword(1L, rawOldPassword, rawNewPassword);

            //then
            String encodedUpdatePassword = shelter.getPassword();
            boolean match = passwordEncoder.matchesPassword(encodedUpdatePassword, rawNewPassword);
            assertThat(match).isTrue();
        }
    }

    @Nested
    @DisplayName("updateAddressStatus 메서드 호출 시")
    class UpdateAddressStatusTest {

        @Test
        @DisplayName("성공")
        void updateAddressStatus() {
            //given
            Shelter shelter = shelter();
            boolean updateAddressStatus = false;

            given(shelterRepository.findById(anyLong())).willReturn(Optional.of(shelter));

            //when
            shelterService.updateAddressStatus(1L, updateAddressStatus);

            //then
            assertThat(shelter.isOpenedAddress()).isFalse();
        }
    }
}
