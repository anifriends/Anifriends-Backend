package com.clova.anifriends.domain.image;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record UploadImagesRequest(
    @NotNull(message = "이미지는 필수 입력 항목입니다.") List<MultipartFile> images
) {

}
