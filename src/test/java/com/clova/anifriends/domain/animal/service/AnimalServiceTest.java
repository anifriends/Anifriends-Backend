package com.clova.anifriends.domain.animal.service;

import static com.clova.anifriends.domain.animal.support.fixture.AnimalFixture.ANIMAL_TYPE;
import static com.clova.anifriends.domain.animal.support.fixture.AnimalFixture.animal;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.AnimalAge;
import com.clova.anifriends.domain.animal.AnimalSize;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalDetail;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsByShelterResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse;
import com.clova.anifriends.domain.animal.exception.AnimalNotFoundException;
import com.clova.anifriends.domain.animal.repository.AnimalCacheRepository;
import com.clova.anifriends.domain.animal.repository.AnimalRepository;
import com.clova.anifriends.domain.animal.support.fixture.AnimalFixture;
import com.clova.anifriends.domain.animal.vo.AnimalActive;
import com.clova.anifriends.domain.animal.vo.AnimalGender;
import com.clova.anifriends.domain.animal.vo.AnimalNeuteredFilter;
import com.clova.anifriends.domain.animal.vo.AnimalType;
import com.clova.anifriends.domain.common.event.ImageDeletionEvent;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @InjectMocks
    AnimalService animalService;

    @Mock
    AnimalCacheRepository animalCacheRepository;

    @Mock
    AnimalRepository animalRepository;

    @Mock
    ShelterRepository shelterRepository;

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

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
            then(animalCacheRepository).should().saveAnimal(any());
            then(animalRepository).should().save(any());
            then(animalRepository).should().save(any());
            then(animalCacheRepository).should().increaseTotalNumberOfAnimals();
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
    @DisplayName("findAnimalDetail 실행 시")
    class FindAnimalDetailTest {

        @Test
        @DisplayName("성공")
        void findAnimalDetail() {
            // given
            Shelter shelter = shelter();
            Animal animal = animal(shelter);
            FindAnimalDetail expected = FindAnimalDetail.from(animal);

            when(animalRepository.findById(anyLong())).thenReturn(Optional.of(animal));

            // when
            FindAnimalDetail result = animalService.findAnimalDetail(
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
                () -> animalService.findAnimalDetail(anyLong()));

            // then
            assertThat(exception).isInstanceOf(AnimalNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findAnimalsByShelter 실행 시")
    class FindAnimalsByShelterTest {

        @Test
        @DisplayName("성공")
        void findAnimalsByShelter() {
            // given
            Long shelterId = 1L;
            String keyword = "animalName";
            AnimalType type = AnimalType.DOG;
            AnimalGender gender = AnimalGender.MALE;
            AnimalNeuteredFilter neuteredFilter = AnimalNeuteredFilter.IS_NEUTERED;
            AnimalActive active = AnimalActive.ACTIVE;
            AnimalSize size = AnimalSize.SMALL;
            AnimalAge age = AnimalAge.BABY;
            Shelter shelter = shelter();
            Animal animal = animal(shelter);
            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<Animal> pageResult = new PageImpl<>(List.of(animal));
            FindAnimalsByShelterResponse expected = FindAnimalsByShelterResponse.from(pageResult);

            given(
                animalRepository.findAnimalsByShelter(shelterId, keyword, type, gender,
                    neuteredFilter, active, size, age, pageRequest))
                .willReturn(pageResult);

            // when
            FindAnimalsByShelterResponse animalsByShelter = animalService.findAnimalsByShelter(
                shelterId, keyword, type, gender, neuteredFilter,
                active, size, age, pageRequest);

            // then
            assertThat(expected).usingRecursiveComparison().isEqualTo(animalsByShelter);
        }
    }

    @Nested
    @DisplayName("findAnimals 실행 시")
    class FindAnimalsByVolunteerTest {

        @Test
        @DisplayName("성공: 모든 필터 존재")
        void findAnimalsByVolunteer1() {
            // given
            String mockName = "animalName";
            String mockInformation = "animalInformation";
            String mockBreed = "animalBreed";
            List<String> mockImageUrls = List.of("www.aws.s3.com/2");

            AnimalType typeFilter = AnimalType.DOG;
            AnimalActive activeFilter = AnimalActive.ACTIVE;
            AnimalNeuteredFilter neuteredFilter = AnimalNeuteredFilter.IS_NEUTERED;
            AnimalAge ageFilter = AnimalAge.ADULT;
            AnimalGender genderFilter = AnimalGender.MALE;
            AnimalSize sizeFilter = AnimalSize.MEDIUM;

            Shelter shelter = ShelterFixture.shelter();

            Animal matchAnimal = new Animal(
                shelter,
                mockName,
                LocalDate.now().minusMonths(ageFilter.getMinMonth()),
                typeFilter.getName(),
                mockBreed,
                genderFilter.getName(),
                neuteredFilter.isNeutered(),
                activeFilter.getName(),
                sizeFilter.getMinWeight(),
                mockInformation,
                mockImageUrls
            );

            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<Animal> pageResult = new PageImpl<>(List.of(matchAnimal), pageRequest, 1);

            FindAnimalsResponse expected = FindAnimalsResponse.from(
                pageResult);

            when(animalRepository.findAnimalsByVolunteer(typeFilter, activeFilter,
                neuteredFilter, ageFilter, genderFilter, sizeFilter, pageRequest))
                .thenReturn(pageResult);

            // when
            FindAnimalsResponse result = animalService.findAnimalsByVolunteer(
                typeFilter, activeFilter, neuteredFilter,
                ageFilter, genderFilter, sizeFilter, pageRequest);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);

        }
    }

    @Nested
    @DisplayName("findAnimals 실행 시(캐시 호출 테스트)")
    class FindAnimalsByVolunteerForCacheTest {

        @Test
        @DisplayName("성공: 캐시 호출(모든 파라미터가 존재하지 않음)")
        void findAnimalByVolunteerForCache1() {
            // given
            AnimalType typeFilter = null;
            AnimalActive activeFilter = null;
            AnimalNeuteredFilter neuteredFilter = null;
            AnimalAge ageFilter = null;
            AnimalGender genderFilter = null;
            AnimalSize sizeFilter = null;
            LocalDateTime createdAt = null;
            Long animalId = null;

            // when
            animalService.findAnimalsByVolunteerV2(
                typeFilter, activeFilter, neuteredFilter,
                ageFilter, genderFilter, sizeFilter, createdAt, animalId, PageRequest.of(0, 10));

            // then
            verify(animalCacheRepository, times(1)).findAnimals(anyInt(), anyLong());
            verify(animalCacheRepository, times(1)).getTotalNumberOfAnimals();
        }

        @Test
        @DisplayName("성공: 캐시 미호출 안함(activeFilter 존재)")
        void findAnimalByVolunteerForCache3() {
            // given
            AnimalType typeFilter = null;
            AnimalActive activeFilter = AnimalActive.ACTIVE;
            AnimalNeuteredFilter neuteredFilter = null;
            AnimalAge ageFilter = null;
            AnimalGender genderFilter = null;
            AnimalSize sizeFilter = null;
            LocalDateTime createdAt = null;
            Long animalId = null;

            when(animalRepository.findAnimalsByVolunteerV2(typeFilter, activeFilter,
                neuteredFilter, ageFilter, genderFilter, sizeFilter, createdAt, animalId,
                PageRequest.of(0, 10)))
                .thenReturn(new SliceImpl<>(List.of(), PageRequest.of(0, 10), false));

            // when
            animalService.findAnimalsByVolunteerV2(
                typeFilter, activeFilter, neuteredFilter,
                ageFilter, genderFilter, sizeFilter, createdAt, animalId, PageRequest.of(0, 10));

            // then
            verify(animalCacheRepository, times(0)).findAnimals(anyInt(), anyLong());
        }

    }

    @Nested
    @DisplayName("findAnimalsByVolunteerV2 실행 시")
    class FindAnimalsByVolunteerV2Test {

        @Test
        @DisplayName("성공")
        void findAnimalsByVolunteerV2_1() {
            // given
            String mockName = "animalName";
            String mockInformation = "animalInformation";
            String mockBreed = "animalBreed";
            List<String> mockImageUrls = List.of("www.aws.s3.com/2");

            AnimalType typeFilter = AnimalType.DOG;
            AnimalActive activeFilter = AnimalActive.ACTIVE;
            AnimalNeuteredFilter neuteredFilter = AnimalNeuteredFilter.IS_NEUTERED;
            AnimalAge ageFilter = AnimalAge.ADULT;
            AnimalGender genderFilter = AnimalGender.MALE;
            AnimalSize sizeFilter = AnimalSize.MEDIUM;

            Shelter shelter = ShelterFixture.shelter();

            Animal matchAnimal = new Animal(
                shelter,
                mockName,
                LocalDate.now().minusMonths(ageFilter.getMinMonth()),
                typeFilter.getName(),
                mockBreed,
                genderFilter.getName(),
                neuteredFilter.isNeutered(),
                activeFilter.getName(),
                sizeFilter.getMinWeight(),
                mockInformation,
                mockImageUrls
            );

            PageRequest pageRequest = PageRequest.of(0, 10);
            SliceImpl<Animal> pageResult = new SliceImpl<>(List.of(matchAnimal), pageRequest,
                false);

            FindAnimalsResponse expected = FindAnimalsResponse.fromV2(pageResult, 1L);

            when(animalRepository.findAnimalsByVolunteerV2(typeFilter, activeFilter,
                neuteredFilter, ageFilter, genderFilter, sizeFilter, LocalDateTime.MIN, 0L,
                pageRequest))
                .thenReturn(pageResult);
            when(animalRepository.countAnimalsV2(typeFilter, activeFilter,
                neuteredFilter, ageFilter, genderFilter, sizeFilter))
                .thenReturn(1L);

            // when
            FindAnimalsResponse result = animalService.findAnimalsByVolunteerV2(
                typeFilter, activeFilter, neuteredFilter,
                ageFilter, genderFilter, sizeFilter, LocalDateTime.MIN, 0L, pageRequest);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);

        }

        @Test
        @DisplayName("성공: 페이징 필터만 존재하는 경우")
        void findAnimalsByVolunteerV2OnlyHavePagingFilter() {
            // given
            String mockName = "animalName";
            String mockInformation = "animalInformation";
            String mockBreed = "animalBreed";
            List<String> mockImageUrls = List.of("www.aws.s3.com/2");

            AnimalType typeFilter = null;
            AnimalActive activeFilter = null;
            AnimalNeuteredFilter neuteredFilter = null;
            AnimalAge ageFilter = null;
            AnimalGender genderFilter = null;
            AnimalSize sizeFilter = null;

            Shelter shelter = ShelterFixture.shelter();

            Animal matchAnimal = new Animal(
                shelter,
                mockName,
                LocalDate.now().minusMonths(AnimalAge.ADULT.getMinMonth()),
                AnimalType.DOG.getName(),
                mockBreed,
                AnimalGender.MALE.getName(),
                AnimalNeuteredFilter.IS_NEUTERED.isNeutered(),
                AnimalActive.ACTIVE.getName(),
                AnimalSize.MEDIUM.getMinWeight(),
                mockInformation,
                mockImageUrls
            );

            PageRequest pageRequest = PageRequest.of(0, 10);
            SliceImpl<Animal> pageResult = new SliceImpl<>(List.of(matchAnimal), pageRequest,
                false);

            FindAnimalsResponse expected = FindAnimalsResponse.fromV2(pageResult, 1L);

            when(animalRepository.findAnimalsByVolunteerV2(typeFilter, activeFilter,
                neuteredFilter, ageFilter, genderFilter, sizeFilter, LocalDateTime.MIN, 0L,
                pageRequest))
                .thenReturn(pageResult);
            when(animalRepository.countAnimalsV2(typeFilter, activeFilter,
                neuteredFilter, ageFilter, genderFilter, sizeFilter))
                .thenReturn(1L);

            // when
            FindAnimalsResponse result = animalService.findAnimalsByVolunteerV2(
                typeFilter, activeFilter, neuteredFilter,
                ageFilter, genderFilter, sizeFilter, LocalDateTime.MIN, 0L, pageRequest);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);

        }

        @Test
        @DisplayName("성공: 타입, 종이 null일 경우")
        void findAnimalsByVolunteerV2TwoFilterIsNull() {
            // given
            String mockName = "animalName";
            String mockInformation = "animalInformation";
            String mockBreed = "animalBreed";
            List<String> mockImageUrls = List.of("www.aws.s3.com/2");

            AnimalType typeFilter = null;
            AnimalActive activeFilter = null;
            AnimalNeuteredFilter neuteredFilter = AnimalNeuteredFilter.IS_NEUTERED;
            AnimalAge ageFilter = AnimalAge.ADULT;
            AnimalGender genderFilter = AnimalGender.MALE;
            AnimalSize sizeFilter = AnimalSize.MEDIUM;

            Shelter shelter = ShelterFixture.shelter();

            Animal matchAnimal = new Animal(
                shelter,
                mockName,
                LocalDate.now().minusMonths(ageFilter.getMinMonth()),
                ANIMAL_TYPE,
                mockBreed,
                genderFilter.getName(),
                neuteredFilter.isNeutered(),
                AnimalActive.ACTIVE.getName(),
                sizeFilter.getMinWeight(),
                mockInformation,
                mockImageUrls
            );

            PageRequest pageRequest = PageRequest.of(0, 10);
            SliceImpl<Animal> pageResult = new SliceImpl<>(List.of(matchAnimal), pageRequest,
                false);

            FindAnimalsResponse expected = FindAnimalsResponse.fromV2(pageResult, 1L);

            when(animalRepository.findAnimalsByVolunteerV2(typeFilter, activeFilter,
                neuteredFilter, ageFilter, genderFilter, sizeFilter, LocalDateTime.MIN, 0L,
                pageRequest))
                .thenReturn(pageResult);
            when(animalRepository.countAnimalsV2(typeFilter, activeFilter,
                neuteredFilter, ageFilter, genderFilter, sizeFilter))
                .thenReturn(1L);

            // when
            FindAnimalsResponse result = animalService.findAnimalsByVolunteerV2(
                typeFilter, activeFilter, neuteredFilter,
                ageFilter, genderFilter, sizeFilter, LocalDateTime.MIN, 0L, pageRequest);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);

        }
    }

    @Nested
    @DisplayName("updateAnimalAdoptStatus 실행 시")
    class UpdateAnimalAdoptStatus {

        @Test
        @DisplayName("성공: 입양 상태 true -> false")
        void updateAnimalAdoptStatusMakeFalse() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            boolean originStatus = true;
            boolean updateStatus = false;
            Animal animal = AnimalFixture.animal(shelter, originStatus);

            when(animalRepository.findByShelterIdAndAnimalId(anyLong(), anyLong()))
                .thenReturn(Optional.of(animal));

            // when
            Exception exception = catchException(
                () -> animalService.updateAnimalAdoptStatus(anyLong(), anyLong(), updateStatus));

            // then
            verify(animalCacheRepository, never()).deleteAnimal(any());
            verify(animalCacheRepository, never()).decreaseTotalNumberOfAnimals();
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공: 입양 상태 true 로 변경")
        void updateAnimalAdoptStatus2() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            boolean originStatus = false;
            boolean updateStatus = true;
            Animal animal = AnimalFixture.animal(shelter, originStatus);

            when(animalRepository.findByShelterIdAndAnimalId(anyLong(), anyLong()))
                .thenReturn(Optional.of(animal));

            // when
            Exception exception = catchException(
                () -> animalService.updateAnimalAdoptStatus(anyLong(), anyLong(), updateStatus));

            // then
            verify(animalCacheRepository, times(1)).deleteAnimal(any(Animal.class));
            verify(animalCacheRepository, times(1)).decreaseTotalNumberOfAnimals();
            assertThat(exception).isNull();
        }
    }

    @Nested
    @DisplayName("updateAnimal 메서드 호출 시")
    class UpdateRecruitmentTest {

        Animal animal;

        @BeforeEach
        void setUp() {
            Shelter shelter = ShelterFixture.shelter();
            animal = AnimalFixture.animal(shelter);
        }

        @Test
        @DisplayName("성공: 기존 이미지 2개, 새로운 이미지 1개")
        void updateRecruitment1() {
            //given
            Shelter shelter = ShelterFixture.shelter();
            String originImage1 = "www.aws.s3.com/1";
            String originImage2 = "www.aws.s3.com/2";
            Animal animal = AnimalFixture.animal(shelter, List.of(originImage1, originImage2));

            String mockName = "animalName";
            LocalDate mockBirthDate = LocalDate.now();
            String mockInformation = "animalInformation";
            String mockBreed = "animalBreed";
            String newImage1 = "www.aws.s3.com/3";
            List<String> mockImageUrls = List.of(newImage1);
            AnimalType mockType = AnimalType.DOG;
            AnimalActive mockActive = AnimalActive.ACTIVE;
            Double mockWeight = 1.2;
            Boolean mockIsNeutered = true;
            AnimalGender mockGender = AnimalGender.MALE;

            given(animalRepository.findByAnimalIdAndShelterIdWithImages(anyLong(),
                anyLong())).willReturn(Optional.of(animal));

            //when
            animalService.updateAnimal(1L, 1L,
                mockName,
                mockBirthDate,
                mockType,
                mockBreed,
                mockGender,
                mockIsNeutered,
                mockActive,
                mockWeight,
                mockInformation,
                mockImageUrls);

            //then
            verify(applicationEventPublisher, times(1)).publishEvent(
                new ImageDeletionEvent(List.of(originImage1, originImage2)));

            assertThat(animal.getName()).isEqualTo(mockName);
            assertThat(animal.getBirthDate()).isEqualTo(mockBirthDate);
            assertThat(animal.getType()).isEqualTo(mockType);
            assertThat(animal.getBreed()).isEqualTo(mockBreed);
            assertThat(animal.getGender()).isEqualTo(mockGender);
            assertThat(animal.isNeutered()).isEqualTo(mockIsNeutered);
            assertThat(animal.getActive()).isEqualTo(mockActive);
            assertThat(animal.getWeight()).isEqualTo(mockWeight);
            assertThat(animal.getInformation()).isEqualTo(mockInformation);
            assertThat(animal.getImages()).usingRecursiveComparison().isEqualTo(mockImageUrls);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): 존재하지 않는 animalImage ID")
        void throwExceptionWhenAnimalImageIdIsNotExist() {
            //given
            String mockName = "animalName";
            LocalDate mockBirthDate = LocalDate.now();
            String mockInformation = "animalInformation";
            String mockBreed = "animalBreed";
            List<String> mockImageUrls = List.of("www.aws.s3.com/2");

            AnimalType mockType = AnimalType.DOG;
            AnimalActive mockActive = AnimalActive.ACTIVE;
            Double mockWeight = 1.2;
            Boolean mockIsNeutered = true;
            AnimalGender mockGender = AnimalGender.MALE;

            given(animalRepository.findByAnimalIdAndShelterIdWithImages(anyLong(),
                anyLong())).willReturn(Optional.empty());

            //when
            Exception exception = catchException(
                () -> animalService.updateAnimal(1L, 1L,
                    mockName,
                    mockBirthDate,
                    mockType,
                    mockBreed,
                    mockGender,
                    mockIsNeutered,
                    mockActive,
                    mockWeight,
                    mockInformation,
                    mockImageUrls));

            //then
            assertThat(exception).isInstanceOf(AnimalNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("deleteAnimal 메서드 호출 시")
    class DeleteAnimalTest {

        Animal animal;

        @BeforeEach
        void setUp() {
            Shelter shelter = ShelterFixture.shelter();
            animal = AnimalFixture.animal(shelter);
        }

        @Test
        @DisplayName("성공")
        void deleteAnimal() {
            //given
            List<String> originImages = animal.getImages();
            given(animalRepository.findByShelterIdAndAnimalId(anyLong(), anyLong()))
                .willReturn(Optional.ofNullable(animal));

            //when
            animalService.deleteAnimal(1L, 1L);

            //then
            verify(applicationEventPublisher, times(1)).publishEvent(
                new ImageDeletionEvent(originImages));
            then(animalRepository).should().delete(any(Animal.class));
            verify(animalCacheRepository, times(1)).decreaseTotalNumberOfAnimals();
        }

        @Test
        @DisplayName("예외(AniamlNotFoundException): 존재하지 않는 보호 동물")
        void exceptionWhenAnimalNotFound() {
            //given
            given(animalRepository.findByShelterIdAndAnimalId(anyLong(), anyLong()))
                .willReturn(Optional.empty());

            //when
            Exception exception = catchException(() -> animalService.deleteAnimal(1L, 1L));

            //then
            assertThat(exception).isInstanceOf(AnimalNotFoundException.class);
        }
    }
}
