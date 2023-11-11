package com.clova.anifriends.base;

import com.clova.anifriends.domain.common.ImageRemover;

public class MockImageRemover implements ImageRemover {

    @Override
    public void removeImage(String imageUrl) {
        System.out.println("remove " + imageUrl);
    }
}
