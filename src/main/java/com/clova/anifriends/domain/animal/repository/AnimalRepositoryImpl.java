package com.clova.anifriends.domain.animal.repository;

import static com.clova.anifriends.domain.animal.QAnimal.animal;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.AnimalAge;
import com.clova.anifriends.domain.animal.AnimalSize;
import com.clova.anifriends.domain.animal.vo.AnimalActive;
import com.clova.anifriends.domain.animal.vo.AnimalGender;
import com.clova.anifriends.domain.animal.vo.AnimalNeuteredFilter;
import com.clova.anifriends.domain.animal.vo.AnimalType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
        AnimalNeuteredFilter neuteredFilter,
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
                animalIsNeutered(neuteredFilter),
                animalActiveContains(active),
                animalSizeContains(size),
                animalAgeContains(age)
            )
            .orderBy(animal.createdAt.desc())
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
                animalIsNeutered(neuteredFilter),
                animalActiveContains(active),
                animalSizeContains(size),
                animalAgeContains(age)
            )
            .fetchOne();

        return new PageImpl<>(animals, pageable, count == null ? 0 : count);
    }

    @Override
    public Page<Animal> findAnimalsByVolunteer(
        AnimalType type,
        AnimalActive active,
        AnimalNeuteredFilter neuteredFilter,
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
                animalIsNeutered(neuteredFilter),
                animalAgeContains(age),
                animalGenderContains(gender),
                animalSizeContains(size)
            )
            .orderBy(animal.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = query.select(animal.count())
            .from(animal)
            .join(animal.shelter)
            .where(
                animalTypeContains(type),
                animalActiveContains(active),
                animalIsNeutered(neuteredFilter),
                animalAgeContains(age),
                animalGenderContains(gender),
                animalSizeContains(size)
            )
            .fetchOne();

        return new PageImpl<>(animals, pageable, count == null ? 0 : count);
    }

    @Override
    public Slice<Animal> findAnimalsByVolunteerV2(
        AnimalType type,
        AnimalActive active,
        AnimalNeuteredFilter neuteredFilter,
        AnimalAge age,
        AnimalGender gender,
        AnimalSize size,
        LocalDateTime createdAt,
        Long animalId,
        Pageable pageable
    ) {
        List<Animal> animals = query.selectFrom(animal)
            .join(animal.shelter)
            .where(
                animalTypeContains(type),
                animalActiveContains(active),
                animalIsNeutered(neuteredFilter),
                animalAgeContains(age),
                animalGenderContains(gender),
                animalSizeContains(size),
                cursorId(animalId, createdAt)
            )
            .orderBy(animal.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1L)
            .fetch();

        boolean hasNext = hasNext(pageable.getPageSize(), animals);
        return new SliceImpl<>(animals, pageable, hasNext);
    }

    private BooleanExpression cursorId(Long animalId, LocalDateTime createdAt) {
        if (animalId == null || createdAt == null) {
            return null;
        }

        return animal.createdAt.lt(createdAt)
            .or(
                animal.animalId.lt(animalId)
                    .and(animal.createdAt.eq(createdAt)
                    )
            );
    }

    private boolean hasNext(int pageSize, List<Animal> animals) {
        if (animals.size() <= pageSize) {
            return false;
        }

        animals.remove(pageSize);
        return true;
    }


    @Override
    public long countAnimalsV2(
        AnimalType type,
        AnimalActive active,
        AnimalNeuteredFilter neuteredFilter,
        AnimalAge age,
        AnimalGender gender,
        AnimalSize size
    ) {
        Long count = query.select(animal.count())
            .from(animal)
            .join(animal.shelter)
            .where(
                animalTypeContains(type),
                animalActiveContains(active),
                animalIsNeutered(neuteredFilter),
                animalAgeContains(age),
                animalGenderContains(gender),
                animalSizeContains(size)
            )
            .fetchOne();

        return count == null ? 0 : count;
    }

    @Override
    public long countAllAnimalsExceptAdopted() {
        Long count = query.select(animal.count())
            .from(animal)
            .where(animal.adopted.isAdopted.eq(false))
            .fetchOne();
        return count != null ? count : 0;
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
        AnimalNeuteredFilter neuteredFilter
    ) {
        return neuteredFilter != null ? animal.neutered.isNeutered.eq(neuteredFilter.isNeutered())
            : null;
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
