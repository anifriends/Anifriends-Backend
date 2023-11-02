package com.clova.anifriends.domain.common.dto;

import org.springframework.data.domain.Page;

public record PageInfo(
    long totalElements,
    boolean hasNext
) {

    public static PageInfo from(Page page) {
        return new PageInfo(
            page.getTotalElements(),
            page.hasNext()
        );
    }
}
