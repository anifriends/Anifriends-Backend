package com.clova.anifriends.domain.animal.repository;

import static com.clova.anifriends.domain.animal.QAnimal.animal;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.AnimalAge;
import com.clova.anifriends.domain.animal.AnimalSize;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
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
                keywordContains(keyword),
                optionSearch(type, gender, isNeutered, active, size, age)
            )
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        Long count = query.select(animal.count())
            .from(animal)
            .where(
                animal.shelter.shelterId.eq(shelterId),
                keywordContains(keyword),
                optionSearch(type, gender, isNeutered, active, size, age)
            )
            .fetchOne();

        return new PageImpl<>(animals, pageable, count);
    }

    private BooleanBuilder keywordContains(String keyword) {
        return nullSafeBuilder(() -> animalNameContains(keyword));
    }

    private BooleanExpression animalNameContains(String keyword) {
        return animal.name.name.contains(keyword);
    }

    private BooleanExpression animalTypeContains(
        AnimalType type
    ) {
        return animal.type.stringValue().eq(type.getName());
    }

    private BooleanExpression animalGenderContains(
        AnimalGender gender
    ) {
        return gender != null ? animal.gender.stringValue().eq(gender.getName()) : null;
    }

    private BooleanExpression animalIsNeutered(
        Boolean isNeuteredFilter
    ) {
        return isNeuteredFilter != null ? animal.neutered.isNeutered.eq(isNeuteredFilter) : null;
    }

    private BooleanExpression animalActiveContains(
        AnimalActive active
    ) {
        return active != null ? animal.active.stringValue().contains(active.getName()) : null;
    }

    private BooleanExpression animalSizeContains(
        AnimalSize size
    ) {
        if (Objects.isNull(size)) {
            return null;
        }

        int minWeight = size.getMinWeight();
        int maxWeight = size.getMaxWeight();

        return animal.weight.weight.between(minWeight, maxWeight);
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

        return animal.birthDate.between(minDate, maxDate);
    }

    private BooleanBuilder optionSearch(
        AnimalType type,
        AnimalGender gender,
        Boolean isNeutered,
        AnimalActive active,
        AnimalSize size,
        AnimalAge age
    ) {
        return nullSafeBuilder(() -> animalTypeContains(type))
            .or(nullSafeBuilder(() -> animalIsNeutered(isNeutered)))
            .or(nullSafeBuilder(() -> animalGenderContains(gender)))
            .or(nullSafeBuilder(() -> animalActiveContains(active)))
            .or(nullSafeBuilder(() -> animalSizeContains(size)))
            .or(nullSafeBuilder(() -> animalAgeContains(age)))
            ;
    }

    private BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> supplier) {
        try {
            return new BooleanBuilder(supplier.get());
        } catch (IllegalArgumentException e) {
            return new BooleanBuilder();
        }
    }
}
