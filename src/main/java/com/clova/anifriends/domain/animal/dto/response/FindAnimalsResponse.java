package com.clova.anifriends.domain.animal.dto.response;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.common.PageInfo;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindAnimalsResponse(
    PageInfo pageInfo,
    List<FindAnimalByVolunteerResponse> animals
) {

    public record FindAnimalByVolunteerResponse(
        Long animalId,
        String animalName,
        String shelterName,
        String shelterAddress,
        String animalImageUrl
    ) {

        public static FindAnimalByVolunteerResponse from(Animal animal) {
            return new FindAnimalByVolunteerResponse(
                animal.getAnimalId(),
                animal.getName(),
                animal.getShelter().getName(),
                animal.getShelter().getAddress(),
                animal.getImages().get(0)
            );
        }
    }

    public static FindAnimalsResponse from(Page<Animal> pagination) {
        PageInfo pageInfo = PageInfo.of(pagination.getTotalElements(), pagination.hasNext());
        List<FindAnimalByVolunteerResponse> findAnimalByVolunteerResponses = pagination.get()
            .map(FindAnimalByVolunteerResponse::from).toList();

        return new FindAnimalsResponse(pageInfo, findAnimalByVolunteerResponses);
    }

}
