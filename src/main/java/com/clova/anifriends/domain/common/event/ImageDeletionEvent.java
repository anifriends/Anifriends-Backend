package com.clova.anifriends.domain.common.event;

import java.util.List;

public record ImageDeletionEvent(
    List<String> imageUrls
) {

}
