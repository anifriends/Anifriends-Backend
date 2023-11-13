package com.clova.anifriends.domain.animal.dto.response;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.common.PageInfo;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindAnimalsByShelterResponse(
    List<FindAnimalByShelterResponse> animals,
    PageInfo pageInfo
) {

    public record FindAnimalByShelterResponse(
        Long animalId,
        String animalName,
        String animalImageUrl,
        LocalDate animalBirthDate,
        String animalGender,
        boolean animalIsAdopted,
        boolean animalIsNeutered
    ) {

        public static FindAnimalByShelterResponse from(
            Animal animal
        ) {
            return new FindAnimalByShelterResponse(
                animal.getAnimalId(),
                animal.getName(),
                animal.getImages().get(0),
                animal.getBirthDate(),
                animal.getGender().getName(),
                animal.isAdopted(),
                animal.isNeutered()
            );
        }
    }

    public static FindAnimalsByShelterResponse from(
        Page<Animal> animals
    ) {
        PageInfo pageInfo = PageInfo.of(animals.getTotalElements(), animals.hasNext());
        List<FindAnimalByShelterResponse> content = animals.getContent()
            .stream()
            .map(FindAnimalByShelterResponse::from)
            .toList();

        return new FindAnimalsByShelterResponse(content, pageInfo);
    }
}
