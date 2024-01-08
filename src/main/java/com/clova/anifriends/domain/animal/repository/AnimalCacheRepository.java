package com.clova.anifriends.domain.animal.repository;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse;
import com.clova.anifriends.domain.animal.repository.response.FindAnimalsResult;

public interface AnimalCacheRepository {

    Long getTotalNumberOfAnimals();

    void saveAnimal(FindAnimalsResult animal);

    void saveAnimal(Animal animal);

    void deleteAnimal(Animal animal);

    FindAnimalsResponse findAnimals(int size, long count);

    void increaseTotalNumberOfAnimals();

    void decreaseTotalNumberOfAnimals();
}
