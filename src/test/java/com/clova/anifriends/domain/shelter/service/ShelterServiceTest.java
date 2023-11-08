package com.clova.anifriends.domain.shelter.service;

import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.ShelterImage;
import com.clova.anifriends.domain.shelter.dto.CheckDuplicateShelterResponse;
import com.clova.anifriends.domain.shelter.dto.FindShelterDetailResponse;
import com.clova.anifriends.domain.shelter.dto.FindShelterMyPageResponse;
import com.clova.anifriends.domain.shelter.dto.FindShelterSimpleByVolunteerResponse;
import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.shelter.support.ShelterImageFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class ShelterServiceTest {

    @InjectMocks
    private ShelterService shelterService;

    @Mock
    private ShelterRepository shelterRepository;

    @Spy
    PasswordEncoder passwordEncoder = new PasswordEncoder() {
        @Override
        public String encode(CharSequence rawPassword) {
            return new StringBuilder(rawPassword).reverse().toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return encode(rawPassword).equals(encodedPassword);
        }
    };

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

        @Test
        @DisplayName("예외(ShelterBadRequestException): 패스워드가 null")
        void exceptionWhenPasswordIsNull() {
            //given
            String nullPassword = null;

            //when
            Exception exception = catchException(
                () -> shelterService.registerShelter(email, nullPassword, name, address,
                    addressDetail, phoneNumber, sparePhoneNumber, isOpenedAddress));

            //then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @ParameterizedTest
        @CsvSource({
            "4", "5", "17", "18"
        })
        @DisplayName("예외(ShelterBadRequestException): 패스워드가 6자 미만, 16자 초과")
        void exceptionWhenPasswordOutOfLength(String passwordLength) {
            //given
            String passwordOutOfLength = "a".repeat(Integer.parseInt(passwordLength));

            //when
            Exception exception = catchException(
                () -> shelterService.registerShelter(email, passwordOutOfLength, name, address,
                    addressDetail, phoneNumber, sparePhoneNumber, isOpenedAddress));

            //then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
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
    @DisplayName("findShelterSimpleByVolunteer 실행 시")
    class FindShelterSimpleByVolunteerTest {

        @Test
        @DisplayName("성공")
        void findShelterSimpleByVolunteer() {
            // given
            Shelter shelter = shelter();
            FindShelterSimpleByVolunteerResponse expected = FindShelterSimpleByVolunteerResponse.from(
                shelter);

            given(shelterRepository.findById(anyLong())).willReturn(Optional.of(shelter));

            // when
            FindShelterSimpleByVolunteerResponse foundShelterByVolunteerReview = shelterService.findShelterSimpleByVolunteer(
                anyLong());

            // then
            assertThat(foundShelterByVolunteerReview).usingRecursiveComparison()
                .ignoringFields("recruitmentId")
                .isEqualTo(expected);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 존재하지 않는 모집글")
        void throwExceptionWhenRecruitmentIsNotExist() {
            // given
            when(shelterRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> shelterService.findShelterSimpleByVolunteer(anyLong()));

            // then
            assertThat(exception).isInstanceOf(ShelterNotFoundException.class);
        }
    }
}
