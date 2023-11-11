package com.clova.anifriends.domain.animal.wrapper;

import com.clova.anifriends.domain.animal.exception.AnimalBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimalAdopted {

    @Column(name = "is_adopted", nullable = false)
    private Boolean isAdopted = false;

    public AnimalAdopted(Boolean isAdopted) {
        this.isAdopted = isAdopted;
    }

    public AnimalAdopted updateAdoptStatus(boolean isAdopted) {
        validateAdoptStatus(isAdopted);
        return new AnimalAdopted(isAdopted);
    }

    private void validateAdoptStatus(boolean isAdopted) {
        if (isAdopted == true && this.isAdopted == true) {
            throw new AnimalBadRequestException("이미 입양이 완료된 보호 동물입니다");
        }

        if (isAdopted == false && this.isAdopted == false) {
            throw new AnimalBadRequestException("입양이 완료된 보호동물이 아닙니다.");
        }
    }
}
