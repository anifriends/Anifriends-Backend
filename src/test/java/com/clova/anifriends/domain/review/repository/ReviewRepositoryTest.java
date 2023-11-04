package com.clova.anifriends.domain.review.repository;

import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.review.support.ReviewFixture.review;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static com.clova.anifriends.domain.volunteer.support.VolunteerFixture.volunteer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReviewRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Test
    @DisplayName("성공")
    void findByIdAndVolunteerId() {
        //given
        Shelter shelter = shelter();
        Volunteer volunteer = volunteer();
        Recruitment recruitment = recruitment(shelter);
        Review review = review(recruitment, volunteer);

        shelterRepository.save(shelter);
        volunteerRepository.save(volunteer);
        recruitmentRepository.save(recruitment);
        reviewRepository.save(review);

        //when
        Optional<Review> persistedReview = reviewRepository.findByReviewIdAndVolunteerId(
            review.getReviewId(), volunteer.getVolunteerId());

        //then
        assertThat(persistedReview).isEqualTo(Optional.of(review));
    }

    @Test
    @DisplayName("성공")
    void findAllByVolunteerVolunteerIdOrderByCreatedAtDesc() {
        //given
        Shelter shelter = shelter();
        Volunteer volunteer1 = volunteer();
        setField(volunteer1, "volunteerId", 1L);
        Volunteer volunteer2 = volunteer();
        setField(volunteer2, "volunteerId", 2L);
        Recruitment recruitment = recruitment(shelter);
        Review review1 = review(recruitment, volunteer1);
        Review review2 = review(recruitment, volunteer2);

        shelterRepository.save(shelter);
        volunteerRepository.save(volunteer1);
        volunteerRepository.save(volunteer2);
        recruitmentRepository.save(recruitment);
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        //when
        List<Review> persistedReview = reviewRepository.findAllByVolunteerVolunteerIdOrderByCreatedAtDesc(
            volunteer1.getVolunteerId(), null).getContent();

        //then
        assertThat(persistedReview).isEqualTo(List.of(review1));
    }
}
