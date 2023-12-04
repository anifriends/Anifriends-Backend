package com.clova.anifriends.domain.animal.vo;

import com.clova.anifriends.domain.animal.exception.AnimalBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimalNeutered {

    @Column(name = "is_neutered", nullable = false)
    private Boolean isNeutered;

    public AnimalNeutered(Boolean value) {
        validateIsNeutered(value);

        this.isNeutered = value;
    }

    public AnimalNeutered updateIsNeutered(
        Boolean isNeutered
    ) {
        validateIsNeutered(isNeutered);

        return new AnimalNeutered(isNeutered);
    }

    public void validateIsNeutered(
        Boolean isNeutered
    ) {
        if (Objects.isNull(isNeutered)) {
            throw new AnimalBadRequestException("중성화 여부는 필수값입니다.");
        }
    }
}
