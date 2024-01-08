package com.clova.anifriends.domain.animal.service;

import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse.FindAnimalResponse;
import com.clova.anifriends.domain.animal.repository.response.FindAnimalsResult;
import com.clova.anifriends.domain.common.PageInfo;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class AnimalMapper {

    public static FindAnimalsResponse resultToResponse(Page<FindAnimalsResult> animalPage) {
        List<FindAnimalResponse> content = animalPage
            .map(result -> new FindAnimalResponse(
                result.getAnimalId(),
                result.getAnimalName(),
                result.getShelterName(),
                result.getShelterAddress(),
                result.getAnimalImageUrl()
            )).toList();
        PageInfo pageInfo = PageInfo.of(animalPage.getTotalElements(), animalPage.hasNext());
        return new FindAnimalsResponse(pageInfo, content);
    }
}
