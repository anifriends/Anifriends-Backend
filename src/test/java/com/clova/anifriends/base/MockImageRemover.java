package com.clova.anifriends.base;

import com.clova.anifriends.domain.common.ImageRemover;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockImageRemover implements ImageRemover {

    @Override
    public void removeImage(String imageUrl) {
        System.out.println("remove " + imageUrl);
    }

    @Override
    public void removeImages(List<String> imageUrls) {
        System.out.println("remove Images");
    }
}
