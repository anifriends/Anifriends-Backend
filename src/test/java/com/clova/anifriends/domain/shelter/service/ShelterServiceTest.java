package com.clova.anifriends.domain.shelter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.ShelterImage;
import com.clova.anifriends.domain.shelter.dto.FindShelterDetailResponse;
import com.clova.anifriends.domain.shelter.dto.FindShelterMyPageResponse;
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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShelterServiceTest {

    @InjectMocks
    private ShelterService shelterService;

    @Mock
    private ShelterRepository shelterRepository;

    Shelter givenShelter;
    ShelterImage givenShelterImage;

    @Nested
    @DisplayName("findShelterDetail 실행 시")
    class findShelterDetailTest {

        @Test
        @DisplayName("성공")
        void findShelterDetail() {
            //given
            givenShelter = ShelterFixture.shelter();
            givenShelterImage = ShelterImageFixture.shelterImage(givenShelter);
            givenShelter.setShelterImage(givenShelterImage);

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

            given(shelterRepository.findById(any())).willReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> shelterService.findShelterDetail(shelterId));

            // then
            assertThat(exception).isInstanceOf(ShelterNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findShelterMyPage 실행 시")
    class findShelterMyPageTest {

        @Test
        @DisplayName("성공")
        void findShelterMyPage() {
            //given
            givenShelter = ShelterFixture.shelter();
            givenShelterImage = ShelterImageFixture.shelterImage(givenShelter);
            givenShelter.setShelterImage(givenShelterImage);

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

            given(shelterRepository.findById(any())).willReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> shelterService.findShelterMyPage(shelterId));

            // then
            assertThat(exception).isInstanceOf(ShelterNotFoundException.class);
        }
    }
}
