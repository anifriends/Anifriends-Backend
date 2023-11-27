package com.clova.anifriends.domain.animal.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.AnimalAge;
import com.clova.anifriends.domain.animal.AnimalSize;
import com.clova.anifriends.domain.animal.support.fixture.AnimalFixture;
import com.clova.anifriends.domain.animal.vo.AnimalActive;
import com.clova.anifriends.domain.animal.vo.AnimalGender;
import com.clova.anifriends.domain.animal.vo.AnimalNeuteredFilter;
import com.clova.anifriends.domain.animal.vo.AnimalType;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.util.ReflectionTestUtils;

public class AnimalRepositoryTest extends BaseRepositoryTest {

    @Nested
    @DisplayName("findAnimalsByShelter 실행 시")
    class FindAnimalsByShelterTest {

        @Test
        @DisplayName("성공: 이름, 크기가 일치")
        void findAnimalsWhenNameAndSizeAreSame() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            ReflectionTestUtils.setField(shelter, "shelterId", 1L);
            Animal animal = AnimalFixture.animal(shelter);
            Animal animalNotFound = new Animal(
                shelter,
                "animalName",
                LocalDate.now().minusMonths(1),
                AnimalType.DOG.getName(),
                "animalBreed",
                AnimalGender.MALE.getName(),
                true,
                AnimalActive.ACTIVE.getName(),
                14,
                "animalInformation",
                List.of("www.aws.s3.com/2", "www.aws.s3.com/2")
            );

            PageRequest pageRequest = PageRequest.of(0, 10);

            shelterRepository.save(shelter);
            animalRepository.save(animal);
            animalRepository.save(animalNotFound);

            // when
            Page<Animal> expected = animalRepository.findAnimalsByShelter(
                shelter.getShelterId(),
                "animalName",
                null,
                null,
                null,
                null,
                AnimalSize.SMALL,
                null,
                pageRequest
            );

            // then
            assertThat(expected.getTotalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("성공: 이름, 나이가 일치")
        void findAnimalsWhenNameAndAgeAreSame() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            LocalDate animalBirthDate = LocalDate.now().minusMonths(1);
            LocalDate animalNotFoundBirthDate = LocalDate.now().minusMonths(19);

            Animal animal = new Animal(
                shelter,
                "animalName",
                animalBirthDate,
                AnimalType.DOG.getName(),
                "animalBreed",
                AnimalGender.MALE.getName(),
                true,
                AnimalActive.ACTIVE.getName(),
                4,
                "animalInformation",
                List.of("www.aws.s3.com/2", "www.aws.s3.com/2")
            );
            Animal animalNotFound = new Animal(
                shelter,
                "animalName",
                animalNotFoundBirthDate,
                AnimalType.DOG.getName(),
                "animalBreed",
                AnimalGender.MALE.getName(),
                true,
                AnimalActive.ACTIVE.getName(),
                14,
                "animalInformation",
                List.of("www.aws.s3.com/2", "www.aws.s3.com/2")
            );
            PageRequest pageRequest = PageRequest.of(0, 10);

            entityManager.persist(shelter);
            entityManager.persist(animal);
            entityManager.persist(animalNotFound);

            // when
            Page<Animal> expected = animalRepository.findAnimalsByShelter(
                shelter.getShelterId(),
                "animalName",
                null,
                null,
                null,
                null,
                null,
                AnimalAge.BABY,
                pageRequest
            );

            // then
            assertThat(expected.getTotalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("성공: 이름, 종류, 성별, 중성화 여부가 일치")
        void findAnimalsWhenNameAndTypeAndGenderAndIsNeuteredAreSame() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            ReflectionTestUtils.setField(shelter, "shelterId", 1L);
            Animal animal = AnimalFixture.animal(shelter);
            Animal animalNotFound = new Animal(
                shelter,
                "animalName",
                LocalDate.now().minusMonths(1),
                AnimalType.DOG.getName(),
                "animalBreed",
                AnimalGender.MALE.getName(),
                true,
                AnimalActive.ACTIVE.getName(),
                14,
                "animalInformation",
                List.of("www.aws.s3.com/2", "www.aws.s3.com/2")
            );

            PageRequest pageRequest = PageRequest.of(0, 10);

            shelterRepository.save(shelter);
            animalRepository.save(animal);
            animalRepository.save(animalNotFound);

            // when
            Page<Animal> expected = animalRepository.findAnimalsByShelter(
                shelter.getShelterId(),
                "animalName",
                AnimalType.DOG,
                AnimalGender.FEMALE,
                AnimalNeuteredFilter.IS_NEUTERED,
                null,
                null,
                null,
                pageRequest
            );

            // then
            assertThat(expected.getTotalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("성공: 이름 일치, 크기는 최소 경계값")
        void findAnimalsWhenNameIsSameAndSizeIsMinBoundary() {
            // given
            String mockInformation = "animalInformation";
            String mockBreed = "animalBreed";
            List<String> mockImageUrls = List.of("www.aws.s3.com/2");

            String keyword = "animalName";
            AnimalSize sizeFilter = AnimalSize.MEDIUM;

            AnimalType nullTypeFilter = null;
            AnimalActive nullActiveFilter = null;
            AnimalNeuteredFilter nullIsNeuteredFilter = null;
            AnimalAge nullAgeFilter = null;
            AnimalGender nullGenderFilter = null;

            Shelter shelter = ShelterFixture.shelter();

            Animal matchAnimal1 = new Animal(
                shelter,
                keyword,
                LocalDate.now(),
                AnimalType.DOG.getName(),
                mockBreed,
                AnimalGender.MALE.getName(),
                true,
                AnimalActive.ACTIVE.getName(),
                AnimalSize.MEDIUM.getMinWeight(),
                mockInformation,
                mockImageUrls
            );

            Animal misMatchAnimal1 = new Animal(
                shelter,
                keyword,
                LocalDate.now(),
                AnimalType.DOG.getName(),
                mockBreed,
                AnimalGender.MALE.getName(),
                true,
                AnimalActive.ACTIVE.getName(),
                AnimalSize.MEDIUM.getMinWeight() - 1,
                mockInformation,
                mockImageUrls
            );

            shelterRepository.save(shelter);
            animalRepository.saveAll(List.of(matchAnimal1, misMatchAnimal1));

            PageRequest pageRequest = PageRequest.of(0, 10);

            // when
            Page<Animal> result = animalRepository.findAnimalsByShelter(
                shelter.getShelterId(),
                keyword,
                nullTypeFilter,
                nullGenderFilter,
                nullIsNeuteredFilter,
                nullActiveFilter,
                sizeFilter,
                nullAgeFilter,
                pageRequest
            );

            // then
            assertThat(result.getContent()).containsExactlyInAnyOrder(matchAnimal1);
        }

        @Test
        @DisplayName("성공: 이름 일치, 크기는 최대 경계값")
        void findAnimalsWhenNameIsSameAndSizeIsMaxBoundary() {
            // given
            String mockInformation = "animalInformation";
            String mockBreed = "animalBreed";
            List<String> mockImageUrls = List.of("www.aws.s3.com/2");

            String keyword = "animalName";
            AnimalSize sizeFilter = AnimalSize.MEDIUM;

            AnimalType nullTypeFilter = null;
            AnimalActive nullActiveFilter = null;
            AnimalNeuteredFilter nullIsNeuteredFilter = null;
            AnimalAge nullAgeFilter = null;
            AnimalGender nullGenderFilter = null;

            Shelter shelter = ShelterFixture.shelter();

            Animal matchAnimal1 = new Animal(
                shelter,
                keyword,
                LocalDate.now(),
                AnimalType.DOG.getName(),
                mockBreed,
                AnimalGender.MALE.getName(),
                true,
                AnimalActive.ACTIVE.getName(),
                AnimalSize.MEDIUM.getMaxWeight() - 1,
                mockInformation,
                mockImageUrls
            );

            Animal misMatchAnimal1 = new Animal(
                shelter,
                keyword,
                LocalDate.now(),
                AnimalType.DOG.getName(),
                mockBreed,
                AnimalGender.MALE.getName(),
                true,
                AnimalActive.ACTIVE.getName(),
                AnimalSize.MEDIUM.getMaxWeight(),
                mockInformation,
                mockImageUrls
            );

            shelterRepository.save(shelter);
            animalRepository.saveAll(List.of(matchAnimal1, misMatchAnimal1));

            PageRequest pageRequest = PageRequest.of(0, 10);

            // when
            Page<Animal> result = animalRepository.findAnimalsByShelter(
                shelter.getShelterId(),
                keyword,
                nullTypeFilter,
                nullGenderFilter,
                nullIsNeuteredFilter,
                nullActiveFilter,
                sizeFilter,
                nullAgeFilter,
                pageRequest
            );

            // then
            assertThat(result.getContent()).containsExactlyInAnyOrder(matchAnimal1);
        }

    }

    @Nested
    @DisplayName("findAnimalsByVolunteer 실행 시")
    class FindAnimalsByVolunteerTest {

        @Test
        @DisplayName("성공: 모든 필터링이 존재")
        void allFilterExist() {
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

            Animal matchAnimal1 = new Animal(
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

            Animal matchAnimal2 = new Animal(
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

            Animal disMatchAnimal1 = new Animal(
                shelter,
                mockName,
                LocalDate.now().minusMonths(ageFilter.getMinMonth()),
                typeFilter.getName(),
                mockBreed,
                genderFilter.getName(),
                neuteredFilter.isNeutered() ? false : true,
                activeFilter.getName(),
                sizeFilter.getMinWeight(),
                mockInformation,
                mockImageUrls
            );

            shelterRepository.save(shelter);
            animalRepository.saveAll(List.of(matchAnimal1, matchAnimal2, disMatchAnimal1));

            PageRequest pageRequest = PageRequest.of(0, 10);

            // when
            Page<Animal> result = animalRepository.findAnimalsByVolunteer(
                typeFilter,
                activeFilter,
                neuteredFilter,
                ageFilter,
                genderFilter,
                sizeFilter,
                pageRequest
            );

            // then
            assertThat(result.getContent()).containsExactlyInAnyOrder(matchAnimal1, matchAnimal2);
        }

        @Test
        @DisplayName("성공: 모든 필터링이 존재하지 않음")
        void allFilterNotExist() {
            // given
            String mockName = "animalName";
            String mockInformation = "animalInformation";
            String mockBreed = "animalBreed";
            List<String> mockImageUrls = List.of("www.aws.s3.com/2");

            AnimalType nullTypeFilter = null;
            AnimalActive nullActiveFilter = null;
            AnimalNeuteredFilter nullIsNeuteredFilter = null;
            AnimalAge nullAgeFilter = null;
            AnimalGender nullGenderFilter = null;
            AnimalSize nullSizeFilter = null;

            Shelter shelter = ShelterFixture.shelter();

            Animal matchAnimal1 = new Animal(
                shelter,
                mockName,
                LocalDate.now(),
                AnimalType.DOG.getName(),
                mockBreed,
                AnimalGender.MALE.getName(),
                true,
                AnimalActive.ACTIVE.getName(),
                AnimalSize.LARGE.getMinWeight(),
                mockInformation,
                mockImageUrls
            );

            Animal matchAnimal2 = new Animal(
                shelter,
                mockName,
                LocalDate.now(),
                AnimalType.ETC.getName(),
                mockBreed,
                AnimalGender.FEMALE.getName(),
                true,
                AnimalActive.NORMAL.getName(),
                AnimalSize.MEDIUM.getMinWeight(),
                mockInformation,
                mockImageUrls
            );

            shelterRepository.save(shelter);
            animalRepository.saveAll(List.of(matchAnimal1, matchAnimal2));

            PageRequest pageRequest = PageRequest.of(0, 10);

            // when
            Page<Animal> result = animalRepository.findAnimalsByVolunteer(
                nullTypeFilter,
                nullActiveFilter,
                nullIsNeuteredFilter,
                nullAgeFilter,
                nullGenderFilter,
                nullSizeFilter,
                pageRequest
            );

            // then
            assertThat(result.getContent()).containsExactlyInAnyOrder(matchAnimal1, matchAnimal2);
        }
    }

    @Nested
    @DisplayName("findByShelterIdAndAnimalId 실행 시")
    class FindByShelterIdAndAnimalIdTest {

        @Test
        @DisplayName("성공")
        void findByShelterIdAndAnimalId() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            Animal animal = AnimalFixture.animal(shelter);

            shelterRepository.save(shelter);
            animalRepository.save(animal);

            // when
            Optional<Animal> result = animalRepository.findByShelterIdAndAnimalId(
                shelter.getShelterId(),
                animal.getAnimalId());

            // then
            assertThat(result).isEqualTo(Optional.of(animal));
        }
    }

    @Nested
    @DisplayName("findAnimalsByVolunteerV2 실행 시")
    class FindAnimalsByVolunteerV2Test {

        @Test
        @DisplayName("성공: 모든 필터링이 존재")
        void allFilterExist() {
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

            Animal matchAnimal1 = new Animal(
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

            Animal matchAnimal2 = new Animal(
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

            Animal disMatchAnimal1 = new Animal(
                shelter,
                mockName,
                LocalDate.now().minusMonths(ageFilter.getMinMonth()),
                typeFilter.getName(),
                mockBreed,
                genderFilter.getName(),
                neuteredFilter.isNeutered() ? false : true,
                activeFilter.getName(),
                sizeFilter.getMinWeight(),
                mockInformation,
                mockImageUrls
            );

            shelterRepository.save(shelter);
            animalRepository.saveAll(List.of(matchAnimal1, matchAnimal2, disMatchAnimal1));

            PageRequest pageRequest = PageRequest.of(0, 10);

            // when
            Slice<Animal> result = animalRepository.findAnimalsByVolunteerV2(
                typeFilter,
                activeFilter,
                neuteredFilter,
                ageFilter,
                genderFilter,
                sizeFilter,
                LocalDateTime.MIN,
                0L,
                pageRequest
            );

            // then
            assertThat(result.hasNext()).isFalse();
            assertThat(result.getContent()).containsExactlyInAnyOrder(matchAnimal1, matchAnimal2);
        }

        @Test
        @DisplayName("성공: 모든 필터링이 존재하지 않음")
        void allFilterNotExist() {
            // given
            String mockName = "animalName";
            String mockInformation = "animalInformation";
            String mockBreed = "animalBreed";
            List<String> mockImageUrls = List.of("www.aws.s3.com/2");

            AnimalType nullTypeFilter = null;
            AnimalActive nullActiveFilter = null;
            AnimalNeuteredFilter nullIsNeuteredFilter = null;
            AnimalAge nullAgeFilter = null;
            AnimalGender nullGenderFilter = null;
            AnimalSize nullSizeFilter = null;

            Shelter shelter = ShelterFixture.shelter();

            Animal matchAnimal1 = new Animal(
                shelter,
                mockName,
                LocalDate.now(),
                AnimalType.DOG.getName(),
                mockBreed,
                AnimalGender.MALE.getName(),
                true,
                AnimalActive.ACTIVE.getName(),
                AnimalSize.LARGE.getMinWeight(),
                mockInformation,
                mockImageUrls
            );

            Animal matchAnimal2 = new Animal(
                shelter,
                mockName,
                LocalDate.now(),
                AnimalType.ETC.getName(),
                mockBreed,
                AnimalGender.FEMALE.getName(),
                true,
                AnimalActive.NORMAL.getName(),
                AnimalSize.MEDIUM.getMinWeight(),
                mockInformation,
                mockImageUrls
            );

            shelterRepository.save(shelter);
            animalRepository.saveAll(List.of(matchAnimal1, matchAnimal2));

            PageRequest pageRequest = PageRequest.of(0, 10);

            // when
            Slice<Animal> result = animalRepository.findAnimalsByVolunteerV2(
                nullTypeFilter,
                nullActiveFilter,
                nullIsNeuteredFilter,
                nullAgeFilter,
                nullGenderFilter,
                nullSizeFilter,
                LocalDateTime.MIN,
                0L,
                pageRequest
            );

            // then
            assertThat(result.hasNext()).isFalse();
            assertThat(result.getContent()).containsExactlyInAnyOrder(matchAnimal1, matchAnimal2);
        }
    }

    @Nested
    @DisplayName("countAnimalsV2 실행 시")
    class CountAnimalsV2Test {

        @Test
        @DisplayName("성공: 모든 필터링이 존재")
        void allFilterExist() {
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

            Animal matchAnimal1 = new Animal(
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

            Animal matchAnimal2 = new Animal(
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

            Animal disMatchAnimal1 = new Animal(
                shelter,
                mockName,
                LocalDate.now().minusMonths(ageFilter.getMinMonth()),
                typeFilter.getName(),
                mockBreed,
                genderFilter.getName(),
                neuteredFilter.isNeutered() ? false : true,
                activeFilter.getName(),
                sizeFilter.getMinWeight(),
                mockInformation,
                mockImageUrls
            );

            shelterRepository.save(shelter);
            animalRepository.saveAll(List.of(matchAnimal1, matchAnimal2, disMatchAnimal1));

            PageRequest pageRequest = PageRequest.of(0, 10);

            // when
            Long result = animalRepository.countAnimalsV2(
                typeFilter,
                activeFilter,
                neuteredFilter,
                ageFilter,
                genderFilter,
                sizeFilter
            );

            // then
            assertThat(result).isEqualTo(2);
        }

        @Test
        @DisplayName("성공: 모든 필터링이 존재하지 않음")
        void allFilterNotExist() {
            // given
            String mockName = "animalName";
            String mockInformation = "animalInformation";
            String mockBreed = "animalBreed";
            List<String> mockImageUrls = List.of("www.aws.s3.com/2");

            AnimalType nullTypeFilter = null;
            AnimalActive nullActiveFilter = null;
            AnimalNeuteredFilter nullIsNeuteredFilter = null;
            AnimalAge nullAgeFilter = null;
            AnimalGender nullGenderFilter = null;
            AnimalSize nullSizeFilter = null;

            Shelter shelter = ShelterFixture.shelter();

            Animal matchAnimal1 = new Animal(
                shelter,
                mockName,
                LocalDate.now(),
                AnimalType.DOG.getName(),
                mockBreed,
                AnimalGender.MALE.getName(),
                true,
                AnimalActive.ACTIVE.getName(),
                AnimalSize.LARGE.getMinWeight(),
                mockInformation,
                mockImageUrls
            );

            Animal matchAnimal2 = new Animal(
                shelter,
                mockName,
                LocalDate.now(),
                AnimalType.ETC.getName(),
                mockBreed,
                AnimalGender.FEMALE.getName(),
                true,
                AnimalActive.NORMAL.getName(),
                AnimalSize.MEDIUM.getMinWeight(),
                mockInformation,
                mockImageUrls
            );

            shelterRepository.save(shelter);
            animalRepository.saveAll(List.of(matchAnimal1, matchAnimal2));

            PageRequest pageRequest = PageRequest.of(0, 10);

            // when
            Long result = animalRepository.countAnimalsV2(
                nullTypeFilter,
                nullActiveFilter,
                nullIsNeuteredFilter,
                nullAgeFilter,
                nullGenderFilter,
                nullSizeFilter
            );

            // then
            assertThat(result).isEqualTo(2);
        }

    }

    @Nested
    @DisplayName("countAllAnimalsExceptAdopted 실행 시")
    class CountAllAnimalsExceptAdoptedTest {

        @Test
        @DisplayName("성공")
        void countAllAnimalsExceptAdopted() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            Animal animal1 = AnimalFixture.animal(shelter);
            Animal animal2 = AnimalFixture.animal(shelter);
            Animal animal3 = AnimalFixture.animal(shelter, true);
            shelterRepository.save(shelter);
            animalRepository.saveAll(List.of(animal1, animal2, animal3));

            // when
            long count = animalRepository.countAllAnimalsExceptAdopted();

            // then
            assertThat(count).isEqualTo(2L);
        }
    }
}
