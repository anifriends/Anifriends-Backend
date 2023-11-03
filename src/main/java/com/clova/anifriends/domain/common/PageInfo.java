package com.clova.anifriends.domain.common;

public record PageInfo(long totalElements, boolean hasNext) {

    public static PageInfo of(long totalElements, boolean hasNext) {
        return new PageInfo(totalElements, hasNext);
    }
}
