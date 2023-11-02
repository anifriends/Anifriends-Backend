package com.clova.anifriends.domain.animal;

import com.clova.anifriends.domain.animal.exception.AnimalBadRequestException;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimalImages {

    private static final int MAX_SIZE = 5;

    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    List<AnimalImage> images = new ArrayList<>();

    public AnimalImages(List<AnimalImage> images) {
        validateNotNull(images);
        validateImagesSize(images);
        this.images = images;
    }

    private void validateNotNull(List<AnimalImage> images) {
        if (Objects.isNull(images)) {
            throw new AnimalBadRequestException("보호 동물 이미지는 필수값입니다.");
        }
    }

    private void validateImagesSize(List<AnimalImage> images) {
        if (images.isEmpty() || images.size() > MAX_SIZE) {
            throw new AnimalBadRequestException("보호 동물 이미지는 한장 이상, 5장 이하여야 합니다.");
        }
    }
}
