package com.clova.anifriends.domain.review.support;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.volunteer.Volunteer;

public class ReviewFixture {

    public static final String REVIEW_CONTENT = "reviewContent";

    public static Review review(Recruitment recruitment, Volunteer volunteer) {
        return new Review(recruitment, volunteer, REVIEW_CONTENT);

    }

}
