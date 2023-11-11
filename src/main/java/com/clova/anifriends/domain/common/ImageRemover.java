package com.clova.anifriends.domain.common;

import java.util.List;

public interface ImageRemover {

    /**
     * 원격지에 저장된 이미지를 삭제합니다.
     * @param imageUrl 이미지가 저장된 위치를 뜻하는 문자열
     */
    void removeImage(String imageUrl);

    /**
     * 원격지에 저장된 이미지 리스트를 삭제합니다.
     * @param imageUrls 이미지가 저장된 위치를 뜻하는 문자열 리스트
     */
    void removeImages(List<String> imageUrls);
}
