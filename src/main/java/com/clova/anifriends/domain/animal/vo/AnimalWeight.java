package com.clova.anifriends.domain.animal.vo;

import com.clova.anifriends.domain.animal.exception.AnimalBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.text.MessageFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimalWeight {

    private static final double MIN_WEIGHT = 0;
    private static final double MAX_WEIGHT = 99.9;

    @Column(name = "weight")
    private double weight;

    public AnimalWeight(double value) {
        validateWeight(value);
        this.weight = value;
    }

    public AnimalWeight updateWeight(
        Double weight
    ) {
        validateWeight(weight);

        return new AnimalWeight(weight);
    }

    private void validateWeight(double value) {
        if (value <= MIN_WEIGHT || value > MAX_WEIGHT) {
            throw new AnimalBadRequestException(
                MessageFormat.format("몸무게는 {0}보다 크고, {1} 이하여야 합니다.", MIN_WEIGHT, MAX_WEIGHT));
        }
    }
}
