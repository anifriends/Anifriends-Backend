package com.clova.anifriends.domain.animal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.repository.AnimalRepository;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.support.fixture.ShelterFixture;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @InjectMocks
    AnimalService animalService;

    @Mock
    AnimalRepository animalRepository;

    @Mock
    ShelterRepository shelterRepository;

    @Nested
    @DisplayName("registerAnimal 메서드 실행 시")
    class RegisterAnimalTest {

        Shelter shelter = ShelterFixture.shelter();
        RegisterAnimalRequest registerAnimalRequest = new RegisterAnimalRequest(
            "name",
            LocalDate.now(),
            AnimalType.DOG.getName(),
            "품종",
            AnimalGender.FEMALE.getName(),
            false,
            AnimalActive.QUIET.getName(),
            0.7,
            "기타 정보"
        );

        @Test
        @DisplayName("성공")
        void registerAnimal() {
            //given
            given(shelterRepository.findById(anyLong())).willReturn(Optional.ofNullable(shelter));

            //when
            animalService.registerAnimal(1L, registerAnimalRequest);

            //then
            then(animalRepository).should().save(any());
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 존재하지 않는 보호소")
        void exceptionWhenShelterNotFound() {
            //given
            //when
            Exception exception = catchException(
                () -> animalService.registerAnimal(1L, registerAnimalRequest));

            //then
            assertThat(exception).isInstanceOf(ShelterNotFoundException.class);
        }
    }
}