package com.clova.anifriends.domain.recruitment.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentCacheRepository;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class RecruitmentCacheServiceTest {

    @InjectMocks
    RecruitmentCacheService recruitmentCacheService;

    @Mock
    RecruitmentRepository recruitmentRepository;

    @Mock
    RecruitmentCacheRepository recruitmentCacheRepository;

    @Nested
    @DisplayName("synchronizeCache 메서드 실행 시")
    class SynchronizeCacheTest {

        @Test
        @DisplayName("성공")
        void synchronizeCache() {
            //given
            Shelter shelter = ShelterFixture.shelter();
            List<Recruitment> recruitments = RecruitmentFixture.recruitments(shelter, 30);
            PageRequest pageRequest = PageRequest.of(0, 30);
            SliceImpl<Recruitment> recruitmentSlice = new SliceImpl<>(recruitments, pageRequest,
                true);

            given(recruitmentRepository.findRecruitmentsV2(null, null, null, null, true, true, true,
                    null, null, pageRequest))
                .willReturn(recruitmentSlice);

            //when
            recruitmentCacheService.synchronizeCache();

            //then
            then(recruitmentCacheRepository).should(times(30))
                .save(any(Recruitment.class));

        }
    }
}