package com.clova.anifriends.global.image;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record UploadImagesRequest(
    @NotNull(message = "이미지는 필수 입력 항목입니다.")
    @Size(min = 1, max = 5, message = "이미지는 1개 이상 5개 이하로 선택하세요.")
    List<MultipartFile> images
) {

}
