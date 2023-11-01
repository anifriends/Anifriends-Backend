package com.clova.anifriends.domain.animal.service;

import static com.clova.anifriends.domain.animal.support.fixture.AnimalFixture.animal;
import static com.clova.anifriends.domain.shelter.support.fixture.ShelterFixture.shelter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalByVolunteerResponse;
import com.clova.anifriends.domain.animal.exception.NotFoundAnimalException;
import com.clova.anifriends.domain.animal.repository.AnimalRepository;
import com.clova.anifriends.domain.shelter.Shelter;
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
    private AnimalService animalService;

    @Mock
    private AnimalRepository animalRepository;

    @Nested
    @DisplayName("findAnimalByIdByVolunteer 실행 시")
    class FindAnimalTest {

        @Test
        @DisplayName("성공")
        void findAnimalByIdByVolunteer() {
            // given
            Shelter shelter = shelter();
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
            assertThat(exception).isInstanceOf(NotFoundAnimalException.class);
        }
    }

}