package com.clova.anifriends.domain.volunteer.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.dto.request.RegisterVolunteerRequest;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VolunteerServiceTest {

    @InjectMocks
    VolunteerService volunteerService;

    @Mock
    VolunteerRepository volunteerRepository;

    @Nested
    @DisplayName("registerVolunteer 메서드 실행 시")
    class RegisterVolunteerTest {

        Volunteer volunteer = VolunteerFixture.volunteer();

        @Test
        @DisplayName("성공")
        void success() {
            // given
            given(volunteerRepository.save(any())).willReturn(volunteer);
            RegisterVolunteerRequest registerVolunteerRequest = VolunteerFixture.registerVolunteerRequest();

            // when
            volunteerService.registerVolunteer(registerVolunteerRequest);

            // then
            then(volunteerRepository).should().save(any());
        }
    }

}