package com.clova.anifriends.domain.review.support;

import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.ReviewImage;
import com.clova.anifriends.domain.review.dto.request.RegisterReviewRequest;
import com.clova.anifriends.domain.review.dto.response.FindReviewResponse;
import com.clova.anifriends.domain.review.repository.response.FindShelterReviewResult;
import java.time.LocalDateTime;
import java.util.List;

public class ReviewDtoFixture {

    public static FindReviewResponse findReviewResponse(Review review) {
        return FindReviewResponse.from(review);
    }

    public static RegisterReviewRequest registerReviewRequest(Review review) {
        return new RegisterReviewRequest(
            review.getApplicant().getApplicantId(),
            review.getContent(),
            review.getImages()
        );
    }

    public static FindShelterReviewResult findShelterReviewResult(
        Long reviewId, LocalDateTime createdAt, String content, int temperature,
        List<ReviewImage> reviewImages, String volunteerEmail
    ) {
        return
            new FindShelterReviewResult() {
                @Override
                public Long getReviewId() {
                    return reviewId;
                }

                @Override
                public LocalDateTime getCreatedAt() {
                    return createdAt;
                }

                @Override
                public String getContent() {
                    return content;
                }

                @Override
                public List<ReviewImage> getReviewImages() {
                    return reviewImages;
                }

                @Override
                public Long getVolunteerId() {
                    return null;
                }

                @Override
                public String getVolunteerName() {
                    return null;
                }

                @Override
                public String getVolunteerEmail() {
                    return volunteerEmail;
                }

                @Override
                public int getTemperature() {
                    return temperature;
                }

                @Override
                public String getVolunteerImageUrl() {
                    return null;
                }

                @Override
                public Long getVolunteerReviewCount() {
                    return null;
                }
            };
    }

    public static FindShelterReviewResult findShelterReviewResult(
        Long reviewId, LocalDateTime createdAt, String content, Long volunteerId,
        String volunteerName, int temperature, String volunteerImageUrl, Long volunteerReviewCount,
        List<ReviewImage> reviewImages
    ) {
        return new FindShelterReviewResult() {

            @Override
            public Long getReviewId() {
                return reviewId;
            }

            @Override
            public LocalDateTime getCreatedAt() {
                return createdAt;
            }

            @Override
            public String getContent() {
                return content;
            }

            @Override
            public List<ReviewImage> getReviewImages() {
                return reviewImages;
            }

            @Override
            public Long getVolunteerId() {
                return volunteerId;
            }

            @Override
            public String getVolunteerName() {
                return volunteerName;
            }

            @Override
            public String getVolunteerEmail() {
                return null;
            }

            @Override
            public int getTemperature() {
                return temperature;
            }

            @Override
            public String getVolunteerImageUrl() {
                return volunteerImageUrl;
            }

            @Override
            public Long getVolunteerReviewCount() {
                return volunteerReviewCount;
            }
        };
    }
}
