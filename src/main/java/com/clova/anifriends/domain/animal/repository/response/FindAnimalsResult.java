package com.clova.anifriends.domain.animal.repository.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class FindAnimalsResult {

    private final Long animalId;
    private final String animalName;
    private final LocalDateTime createdAt;
    private final String shelterName;
    private final String shelterAddress;
    private final String animalImageUrl;

    @QueryProjection
    public FindAnimalsResult(
        Long animalId,
        String animalName,
        LocalDateTime createdAt,
        String shelterName,
        String shelterAddress,
        String animalImageUrl
    ) {
        this.animalId = animalId;
        this.animalName = animalName;
        this.createdAt = createdAt;
        this.shelterName = shelterName;
        this.shelterAddress = shelterAddress;
        this.animalImageUrl = animalImageUrl;
    }
}
