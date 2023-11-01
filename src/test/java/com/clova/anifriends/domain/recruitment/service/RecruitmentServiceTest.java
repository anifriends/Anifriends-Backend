package com.clova.anifriends.domain.recruitment.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.clova.anifriends.domain.recruitment.dto.RegisterRecruitmentRequest;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecruitmentServiceTest {

    @InjectMocks
    RecruitmentService recruitmentService;

    @Mock
    ShelterRepository shelterRepository;

    @Mock
    RecruitmentRepository recruitmentRepository;

    @Nested
    @DisplayName("registerRecruitment 메서드 실행 시")
    class RegisterRecruitmentTest {

        Long shelterId = 1L;
        String title = "title";
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusMinutes(1);
        LocalDateTime deadline = LocalDateTime.now().plusMinutes(1);
        Integer capacity = 10;
        String content = "content";
        List<String> imageUrls = List.of();

        RegisterRecruitmentRequest request = new RegisterRecruitmentRequest(
            title, startTime, endTime, deadline, capacity, content, imageUrls);

        @Test
        @DisplayName("성공")
        void registerRecruitment() {
            //given
            Shelter shelter = new Shelter("email@email.com", "password123!", "address",
                "addressDetail", "name",
                "0212345678", "0212345678", false);

            given(shelterRepository.findById(any())).willReturn(Optional.of(shelter));

            //when
            recruitmentService.registerRecruitment(shelterId, request);

            //then
            then(recruitmentRepository).should().save(any());
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 존재하지 않는 shelter")
        void exceptionWhenShelterNotFound() {
            //given
            given(shelterRepository.findById(any())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> recruitmentService.registerRecruitment(shelterId, request))
                .isInstanceOf(ShelterNotFoundException.class);
        }
    }
}
