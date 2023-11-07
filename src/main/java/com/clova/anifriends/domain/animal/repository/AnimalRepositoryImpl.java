package com.clova.anifriends.domain.animal.repository;

import static com.clova.anifriends.domain.animal.QAnimal.animal;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.AnimalAge;
import com.clova.anifriends.domain.animal.AnimalSize;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnimalRepositoryImpl implements AnimalRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Page<Animal> findAnimalsByShelter(
        Long shelterId,
        String keyword,
        AnimalType type,
        AnimalGender gender,
        Boolean isNeutered,
        AnimalActive active,
        AnimalSize size,
        AnimalAge age,
        Pageable pageable
    ) {
        List<Animal> animals = query.select(animal)
            .from(animal)
            .where(
                animal.shelter.shelterId.eq(shelterId),
                animalNameContains(keyword),
                animalTypeContains(type),
                animalGenderContains(gender),
                animalIsNeutered(isNeutered),
                animalActiveContains(active),
                animalSizeContains(size),
                animalAgeContains(age)
            )
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        Long count = query.select(animal.count())
            .from(animal)
            .where(
                animal.shelter.shelterId.eq(shelterId),
                animalNameContains(keyword),
                animalTypeContains(type),
                animalGenderContains(gender),
                animalIsNeutered(isNeutered),
                animalActiveContains(active),
                animalSizeContains(size),
                animalAgeContains(age)
            )
            .fetchOne();

        return new PageImpl<>(animals, pageable, count);
    }

    @Override
    public Page<Animal> findAnimalsByVolunteer(
        AnimalType type,
        AnimalActive active,
        Boolean isNeutered,
        AnimalAge age,
        AnimalGender gender,
        AnimalSize size,
        Pageable pageable
    ) {
        List<Animal> animals = query.selectFrom(animal)
            .join(animal.shelter)
            .where(
                animalTypeContains(type),
                animalActiveContains(active),
                animalIsNeutered(isNeutered),
                animalAgeContains(age),
                animalGenderContains(gender),
                animalSizeContains(size)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        int count = query.select(animal.count())
            .from(animal)
            .join(animal.shelter)
            .where(
                animalTypeContains(type),
                animalActiveContains(active),
                animalIsNeutered(isNeutered),
                animalAgeContains(age),
                animalGenderContains(gender),
                animalSizeContains(size)
            )
            .fetch()
            .size();

        return new PageImpl<>(animals, pageable, count);
    }

    private BooleanExpression animalNameContains(String keyword) {
        return keyword != null ? animal.name.name.contains(keyword) : null;
    }

    private BooleanExpression animalTypeContains(
        AnimalType type
    ) {
        return type != null ? animal.type.eq(type) : null;
    }

    private BooleanExpression animalGenderContains(
        AnimalGender gender
    ) {
        return gender != null ? animal.gender.eq(gender) : null;
    }

    private BooleanExpression animalIsNeutered(
        Boolean isNeutered
    ) {
        return isNeutered != null ? animal.neutered.isNeutered.eq(isNeutered) : null;
    }

    private BooleanExpression animalActiveContains(
        AnimalActive active
    ) {
        return active != null ? animal.active.eq(active) : null;
    }

    private BooleanExpression animalSizeContains(
        AnimalSize size
    ) {
        if (Objects.isNull(size)) {
            return null;
        }

        int minWeight = size.getMinWeight();
        int maxWeight = size.getMaxWeight();

        return animal.weight.weight.goe(minWeight)
            .and(animal.weight.weight.lt(maxWeight));
    }

    private BooleanExpression animalAgeContains(
        AnimalAge age
    ) {
        if (Objects.isNull(age)) {
            return null;
        }

        int minMonth = age.getMinMonth();
        int maxMonth = age.getMaxMonth();

        LocalDate currentDate = LocalDate.now();
        LocalDate minDate = currentDate.minusMonths(minMonth);
        LocalDate maxDate = currentDate.minusMonths(maxMonth);

        return animal.birthDate.gt(maxDate)
            .and(animal.birthDate.loe(minDate));
    }
}
