package com.clova.anifriends.domain.common;

public interface ImageRemover {

    /**
     * 원격지에 저장된 이미지를 삭제합니다.
     * @param imageUrl 이미지가 저장된 위치를 뜻하는 문자열
     */
    void removeImage(String imageUrl);
}
