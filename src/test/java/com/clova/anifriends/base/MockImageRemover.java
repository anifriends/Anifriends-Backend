package com.clova.anifriends.base;

import com.clova.anifriends.domain.common.ImageRemover;
import java.util.List;

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
