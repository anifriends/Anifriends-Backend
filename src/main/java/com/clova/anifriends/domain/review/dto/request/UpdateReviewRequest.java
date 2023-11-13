package com.clova.anifriends.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateReviewRequest(
    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    String content,
    @NotNull(message = "이미지는 필수 입력 항목입니다.")
    List<String> imageUrls
) {

}
