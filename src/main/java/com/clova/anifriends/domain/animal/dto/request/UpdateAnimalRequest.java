package com.clova.anifriends.domain.animal.dto.request;

import com.clova.anifriends.domain.animal.vo.AnimalActive;
import com.clova.anifriends.domain.animal.vo.AnimalGender;
import com.clova.anifriends.domain.animal.vo.AnimalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record UpdateAnimalRequest(
    @NotBlank(message = "이름은 공백일 수 없습니다.")
    String name,
    @NotNull(message = "생년월일은 필수값입니다.")
    LocalDate birthDate,
    @NotNull(message = "종류는 공백일 수 없습니다.")
    AnimalType type,
    @NotBlank(message = "품종은 공백일 수 없습니다.")
    String breed,
    @NotNull(message = "성별은 공백일 수 없습니다.")
    AnimalGender gender,
    @NotNull(message = "중성화 여부는 필수값입니다.")
    Boolean isNeutered,
    @NotNull(message = "성격은 공백일 수 없습니다.")
    AnimalActive active,
    @NotNull(message = "무게는 필수값입니다.")
    Double weight,
    @NotBlank(message = "기타 정보는 공백일 수 없습니다.")
    String information,
    @NotNull(message = "이미지는 필수값입니다.")
    List<String> imageUrls
) {

}
