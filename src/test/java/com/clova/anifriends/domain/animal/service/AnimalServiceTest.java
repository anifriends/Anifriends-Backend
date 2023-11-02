package com.clova.anifriends.domain.animal.service;

import static com.clova.anifriends.domain.animal.support.fixture.AnimalFixture.animal;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static com.clova.anifriends.domain.shelter.support.ShelterImageFixture.shelterImage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalByShelterResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalByVolunteerResponse;
import com.clova.anifriends.domain.animal.exception.AnimalNotFoundException;
import com.clova.anifriends.domain.animal.repository.AnimalRepository;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDate;
import java.util.List;
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
        List<String> imageUrls = List.of("www.aws.s3.com/2", "www.aws.s3.com/3");
        RegisterAnimalRequest registerAnimalRequest = new RegisterAnimalRequest(
            "name",
            LocalDate.now(),
            AnimalType.DOG.getName(),
            "품종",
            AnimalGender.FEMALE.getName(),
            false,
            AnimalActive.QUIET.getName(),
            0.7,
            "기타 정보",
            imageUrls
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

    @Nested
    @DisplayName("findAnimalByIdByVolunteer 실행 시")
    class FindAnimalTest {

        @Test
        @DisplayName("성공")
        void findAnimalByIdByVolunteer() {
            // given
            Shelter shelter = shelter();
            shelter.setShelterImage(shelterImage(shelter));
            Animal animal = animal(shelter);
            FindAnimalByVolunteerResponse expected = FindAnimalByVolunteerResponse.from(animal);

            when(animalRepository.findById(anyLong())).thenReturn(Optional.of(animal));

            // when
            FindAnimalByVolunteerResponse result = animalService.findAnimalByIdByVolunteer(
                anyLong());

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        @DisplayName("예외(NotFoundAnimalException): 존재하지 않는 보호 동물")
        void exceptionWhenAnimalIsNotExist() {
            // given
            when(animalRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> animalService.findAnimalByIdByVolunteer(anyLong()));

            // then
            assertThat(exception).isInstanceOf(AnimalNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findAnimalByShelter 메서드 실행 시")
    class FindAnimalByShelterTest {

        Long animalId = 1L;
        Shelter shelter = ShelterFixture.shelter();
        Animal animal = animal(shelter);

        @Test
        @DisplayName("성공")
        void findAnimalByShelter() {
            //given
            given(animalRepository.findByIdWithImages(anyLong())).willReturn(Optional.of(animal));

            //when
            FindAnimalByShelterResponse response = animalService.findAnimalByShelter(
                animalId);

            //then
            assertThat(response.name()).isEqualTo(animal.getName());
            assertThat(response.birthDate()).isEqualTo(animal.getBirthDate());
            assertThat(response.type()).isEqualTo(animal.getType());
            assertThat(response.breed()).isEqualTo(animal.getBreed());
            assertThat(response.gender()).isEqualTo(animal.getGender());
            assertThat(response.isNeutered()).isEqualTo(animal.isNeutered());
            assertThat(response.active()).isEqualTo(animal.getActive());
            assertThat(response.weight()).isEqualTo(animal.getWeight());
            assertThat(response.information()).isEqualTo(animal.getInformation());
            assertThat(response.isAdopted()).isEqualTo(animal.isAdopted());
            assertThat(response.imageUrls()).containsExactlyElementsOf(animal.getImageUrls());
        }

        @Test
        @DisplayName("예외(AnimalNotFoundException): 존재하지 않는 보호 동물")
        void exceptionWhenShelterNotFound() {
            //given
            given(animalRepository.findByIdWithImages(anyLong())).willReturn(Optional.empty());

            //when
            Exception exception = catchException(() -> animalService.findAnimalByShelter(animalId));

            //then
            assertThat(exception).isInstanceOf(AnimalNotFoundException.class);
        }
    }
}
