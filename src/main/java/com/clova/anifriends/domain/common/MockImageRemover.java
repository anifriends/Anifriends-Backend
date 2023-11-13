package com.clova.anifriends.domain.common;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockImageRemover implements ImageRemover {
    @Override
    public void removeImage(String imageUrl) {
        log.info("이미지가 삭제되었습니다.");
    }

    @Override
    public void removeImages(List<String> imageUrls) {
        log.info("이미지 리스트가 삭제되었습니다.");
    }
}
