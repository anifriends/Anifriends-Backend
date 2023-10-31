package com.clova.anifriends.domain.shelter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.ShelterImage;
import com.clova.anifriends.domain.shelter.dto.FindShelterDetailResponse;
import com.clova.anifriends.domain.shelter.exception.ShelterImageNotFoundException;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterImageRepository;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.shelter.support.ShelterImageFixture;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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

    @Mock
    private ShelterImageRepository shelterImageRepository;

    Shelter givenShelter;
    ShelterImage givenShelterImage;

    @BeforeEach
    void setUp() {
        givenShelter = ShelterFixture.shelter();
        givenShelterImage = ShelterImageFixture.shelterImage(givenShelter);
    }

    @Nested
    @DisplayName("findShelterDetail 실행 시")
    class findShelterDetailTest {

        @Test
        @DisplayName("성공")
        void findShelterDetail() {
            //given
            given(shelterRepository.findById(any())).willReturn(Optional.ofNullable(givenShelter));
            given(shelterImageRepository.findShelterImageByShelter(any())).willReturn(
                Optional.ofNullable(givenShelterImage));

            //when
            FindShelterDetailResponse foundShelterDetailResponse = shelterService.findShelterDetail(
                1L);

            //then
            assertThat(givenShelter.getAddress()).isEqualTo(foundShelterDetailResponse.address());
            assertThat(givenShelter.getAddressDetail()).isEqualTo(
                foundShelterDetailResponse.addressDetail());
            assertThat(givenShelter.getName()).isEqualTo(foundShelterDetailResponse.name());
            assertThat(givenShelter.getEmail()).isEqualTo(foundShelterDetailResponse.email());
            assertThat(givenShelter.getPhoneNumber()).isEqualTo(
                foundShelterDetailResponse.phoneNumber());
            assertThat(givenShelter.getSparePhoneNumber()).isEqualTo(
                foundShelterDetailResponse.sparePhoneNumber());
            assertThat(givenShelterImage.getImageUrl()).isEqualTo(
                foundShelterDetailResponse.imageUrl());
        }

        @Test
        @DisplayName("예외: 존재하지 않은 보호소")
        void throwExceptionWhenShelterNotFound() {
            //given
            Long shelterId = 1L;

            given(shelterRepository.findById(any())).willReturn(Optional.empty());

            //when & then
            assertThatThrownBy(() -> shelterService.findShelterDetail(shelterId))
                .isInstanceOf(ShelterNotFoundException.class);
        }

        @Test
        @DisplayName("예외: 존재하지 않은 보호소 이미지")
        void throwExceptionWhenShelterImageNotFound() {
            //given
            Long shelterImageId = 1L;

            given(shelterRepository.findById(any())).willReturn(Optional.ofNullable(givenShelter));

            //when & then
            assertThatThrownBy(() -> shelterService.findShelterDetail(shelterImageId))
                .isInstanceOf(ShelterImageNotFoundException.class);
        }
    }
}
