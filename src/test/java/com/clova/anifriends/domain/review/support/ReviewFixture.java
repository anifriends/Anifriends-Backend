package com.clova.anifriends.domain.review.support;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.review.Review;
import java.util.List;

public class ReviewFixture {

    public static final String REVIEW_CONTENT = "reviewContent";
    public static final List<String> IMAGE_URLS = List.of("imageUrl1", "imageUrl2");

    public static Review review(Applicant applicant) {
        return new Review(applicant, REVIEW_CONTENT, IMAGE_URLS);

    }

    public static Review review(Applicant applicant, List<String> imageUrls) {
        return new Review(applicant, REVIEW_CONTENT, imageUrls);
    }
}
