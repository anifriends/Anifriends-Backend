package com.clova.anifriends.domain.volunteer.service;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.VolunteerImage;
import com.clova.anifriends.domain.volunteer.dto.request.RegisterVolunteerRequest;
import com.clova.anifriends.domain.volunteer.dto.response.FindVolunteerMyPageResponse;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.domain.volunteer.support.VolunteerDtoFixture;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import com.clova.anifriends.domain.volunteer.support.VolunteerImageFixture;
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
            RegisterVolunteerRequest registerVolunteerRequest = VolunteerDtoFixture.registerVolunteerRequest();

            // when
            volunteerService.registerVolunteer(registerVolunteerRequest);

            // then
            then(volunteerRepository).should().save(any());
        }
    }

    @Nested
    @DisplayName("findVolunteerMyPage 메서드 실행 시")
    class FindVolunteerMyPageTest {

        Volunteer volunteer;
        VolunteerImage volunteerImage;

        @Test
        @DisplayName("성공")
        void success() {
            // given
            volunteer = VolunteerFixture.volunteer();
            volunteerImage = VolunteerImageFixture.volunteerImage(volunteer);
            setField(volunteer, "volunteerImage", volunteerImage);
            FindVolunteerMyPageResponse expected = FindVolunteerMyPageResponse.from(volunteer);

            given(volunteerRepository.findById(anyLong())).willReturn(ofNullable(volunteer));

            // when
            FindVolunteerMyPageResponse result = volunteerService.findVolunteerMyPage(1L);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }
    }
}
