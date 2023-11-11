package com.clova.anifriends.domain.recruitment.service;

import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentDtoFixture.findRecruitmentDetailResponse;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentDtoFixture.findRecruitmentsByShelterIdResponse;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentDtoFixture.findRecruitmentsByShelterResponse;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.clova.anifriends.domain.common.ImageRemover;
import com.clova.anifriends.domain.common.MockImageRemover;
import com.clova.anifriends.domain.common.PageInfo;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindCompletedRecruitmentsResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentDetailResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByShelterIdResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByShelterResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByVolunteerResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByVolunteerResponse.FindRecruitmentByVolunteerResponse;
import com.clova.anifriends.domain.recruitment.exception.RecruitmentNotFoundException;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class RecruitmentServiceTest {

    @InjectMocks
    RecruitmentService recruitmentService;

    @Mock
    ShelterRepository shelterRepository;

    @Mock
    RecruitmentRepository recruitmentRepository;

    @Spy
    ImageRemover imageRemover = new MockImageRemover();

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

        @Test
        @DisplayName("성공")
        void registerRecruitment() {
            //given
            Shelter shelter = ShelterFixture.shelter();

            given(shelterRepository.findById(anyLong())).willReturn(Optional.of(shelter));

            //when
            recruitmentService.registerRecruitment(shelterId, title, startTime, endTime, deadline,
                capacity, content, imageUrls);

            //then
            then(recruitmentRepository).should().save(any());
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 존재하지 않는 shelter")
        void exceptionWhenShelterNotFound() {
            //given
            given(shelterRepository.findById(anyLong())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> recruitmentService.registerRecruitment(
                shelterId, title, startTime, endTime, deadline, capacity, content, imageUrls))
                .isInstanceOf(ShelterNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findCompeletedRecruitments 메서드 실행 시")
    class FindCompletedRecruitmentsTest {

        @Test
        @DisplayName("성공")
        void findCompletedRecruitments() {
            //given
            Long volunteerId = 1L;
            PageRequest pageRequest = PageRequest.of(0, 10);
            Shelter shelter = shelter();
            Recruitment recruitment = recruitment(shelter);
            PageImpl<Recruitment> recruitmentPage = new PageImpl<>(List.of(recruitment));
            FindCompletedRecruitmentsResponse expected = FindCompletedRecruitmentsResponse.from(
                recruitmentPage);

            given(recruitmentRepository.findCompletedRecruitments(anyLong(), any()))
                .willReturn(recruitmentPage);

            //when
            FindCompletedRecruitmentsResponse recruitments = recruitmentService.findCompletedRecruitments(
                volunteerId, pageRequest);

            //then
            assertThat(recruitments).usingRecursiveComparison()
                .ignoringFields("recruitmentId")
                .isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("findRecruitmentsByVolunteer 실행 시")
    class FindRecruitmentsByVolunteerTest {

        @Test
        @DisplayName("성공")
        void findRecruitmentsByVolunteer() {
            //give
            String keyword = "keyword";
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.now();
            boolean isClosed = false;
            boolean title = false;
            boolean content = false;
            boolean shelterName = false;
            PageRequest pageRequest = PageRequest.of(0, 10);
            Shelter shelter = shelter();
            Recruitment recruitment = recruitment(shelter);
            PageImpl<Recruitment> recruitments = new PageImpl<>(List.of(recruitment));

            given(recruitmentRepository.findRecruitments(keyword, startDate, endDate, isClosed,
                title, content, shelterName, pageRequest)).willReturn(recruitments);

            //when
            FindRecruitmentsByVolunteerResponse recruitmentsByVolunteer
                = recruitmentService.findRecruitmentsByVolunteer(keyword, startDate, endDate,
                isClosed, title, content, shelterName, pageRequest);

            //then
            PageInfo pageInfo = recruitmentsByVolunteer.pageInfo();
            assertThat(pageInfo.totalElements()).isEqualTo(recruitments.getSize());
            FindRecruitmentByVolunteerResponse findRecruitment = recruitmentsByVolunteer.recruitments()
                .get(0);
            assertThat(findRecruitment.recruitmentTitle()).isEqualTo(recruitment.getTitle());
            assertThat(findRecruitment.recruitmentStartTime()).isEqualTo(
                recruitment.getStartTime());
            assertThat(findRecruitment.recruitmentEndTime()).isEqualTo(recruitment.getEndTime());
            assertThat(findRecruitment.recruitmentApplicantCount()).isEqualTo(
                recruitment.getApplicantCount());
            assertThat(findRecruitment.recruitmentCapacity()).isEqualTo(recruitment.getCapacity());
            assertThat(findRecruitment.shelterName()).isEqualTo(recruitment.getShelter().getName());
            assertThat(findRecruitment.shelterImageUrl())
                .isEqualTo(recruitment.getShelter().getShelterImageUrl());

        }
    }

    @Nested
    @DisplayName("findRecruitmentsByShelter 메서드 실행 시")
    class FindRecruitmentsByShelterTest {

        @Test
        @DisplayName("성공")
        void findRecruitmentsByShelter() {
            // given
            Shelter shelter = shelter();
            Recruitment recruitment = recruitment(shelter);
            setField(recruitment, "recruitmentId", 4L);
            Page<Recruitment> pageResult = new PageImpl<>(List.of(recruitment));
            FindRecruitmentsByShelterResponse expected = findRecruitmentsByShelterResponse(
                pageResult);

            when(recruitmentRepository.findRecruitmentsByShelterOrderByCreatedAt(
                anyLong(), any(), any(), any(), anyBoolean(), anyBoolean(), any()))
                .thenReturn(pageResult);

            // when
            FindRecruitmentsByShelterResponse result = recruitmentService.findRecruitmentsByShelter(
                anyLong(), any(), any(), any(), anyBoolean(), anyBoolean(), any());

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("findRecruitmentsByShelterId 메서드 실행 시")
    class FindRecruitmentsByShelterId {

        @Test
        @DisplayName("성공")
        void findRecruitmentsByShelterId() {
            // given
            Shelter shelter = shelter();
            Recruitment recruitment = recruitment(shelter);
            ReflectionTestUtils.setField(recruitment, "recruitmentId", 4L);
            Page<Recruitment> pageResult = new PageImpl<>(List.of(recruitment));
            FindRecruitmentsByShelterIdResponse expected = findRecruitmentsByShelterIdResponse(
                pageResult);

            when(recruitmentRepository.findRecruitmentsByShelterId(anyLong(), any()))
                .thenReturn(pageResult);

            // when
            FindRecruitmentsByShelterIdResponse result = recruitmentService.findShelterRecruitmentsByShelter(
                anyLong(), any());

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("findRecruitmentDetail 메서드 실행 시")
    class FindRecruitmentDetail {

        @Test
        @DisplayName("성공")
        void findRecruitmentDetail() {
            // given
            Shelter shelter = shelter();
            Recruitment recruitment = recruitment(shelter);
            FindRecruitmentDetailResponse expected = findRecruitmentDetailResponse(recruitment);

            when(recruitmentRepository.findById(anyLong())).thenReturn(Optional.of(recruitment));

            // when
            FindRecruitmentDetailResponse result = recruitmentService.findRecruitmentDetail(
                anyLong());

            // then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("예외(RecruitmentNotFoundException): 존재하지 않는 모집글")
        void throwExceptionWhenRecruitmentIsNotExist() {
            // given
            when(recruitmentRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> recruitmentService.findRecruitmentDetail(anyLong()));

            // then
            assertThat(exception).isInstanceOf(RecruitmentNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("closeRecruitment 메서드 호출 시")
    class CloseRecruitmentTest {

        Recruitment recruitment;

        @BeforeEach
        void setUp() {
            Shelter shelter = shelter();
            recruitment = RecruitmentFixture.recruitment(shelter);
        }

        @Test
        @DisplayName("성공")
        void closeRecruitment() {
            //given
            given(recruitmentRepository.findByShelterIdAndRecruitmentId(anyLong(), anyLong()))
                .willReturn(Optional.ofNullable(recruitment));

            //when
            recruitmentService.closeRecruitment(1L, 1L);

            //then
            assertThat(recruitment.isClosed()).isTrue();
        }

        @Test
        @DisplayName("예외(RecruitmentNotFounException): 존재하지 않는 봉사 모집글")
        void exceptionWhenRecruitmentNotFound() {
            //given
            given(recruitmentRepository.findByShelterIdAndRecruitmentId(anyLong(), anyLong()))
                .willReturn(Optional.empty());

            //when
            Exception exception = catchException(
                () -> recruitmentService.closeRecruitment(1L, 1L));

            //then
            assertThat(exception).isInstanceOf(RecruitmentNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("updateRecruitment 메서드 호출 시")
    class UpdateRecruitmentTest {

        Recruitment recruitment;

        @BeforeEach
        void setUp() {
            Shelter shelter = ShelterFixture.shelter();
            recruitment = RecruitmentFixture.recruitment(shelter);
        }

        @Test
        @DisplayName("성공")
        void updateRecruitment() {
            //given
            String newTitle = recruitment.getTitle() + "a";
            LocalDateTime newStartTime = recruitment.getStartTime().plusDays(1);
            LocalDateTime newEndTime = recruitment.getEndTime().plusDays(1);
            LocalDateTime newDeadline = recruitment.getDeadline().plusDays(1);
            int newCapacity = recruitment.getCapacity() + 1;
            String newContent = recruitment.getContent() + "a";
            List<String> newImageUrls = List.of("a1", "a2");

            given(recruitmentRepository.findByShelterIdAndRecruitmentIdWithImages(anyLong(),
                anyLong())).willReturn(Optional.ofNullable(recruitment));

            //when
            recruitmentService.updateRecruitment(1L, 1L,
                newTitle, newStartTime, newEndTime, newDeadline, newCapacity, newContent,
                newImageUrls);

            //then
            assertThat(recruitment.getTitle()).isEqualTo(newTitle);
            assertThat(recruitment.getStartTime()).isEqualTo(newStartTime);
            assertThat(recruitment.getEndTime()).isEqualTo(newEndTime);
            assertThat(recruitment.getDeadline()).isEqualTo(newDeadline);
            assertThat(recruitment.getCapacity()).isEqualTo(newCapacity);
            assertThat(recruitment.getContent()).isEqualTo(newContent);
            assertThat(recruitment.getImageUrls()).containsExactlyElementsOf(newImageUrls);
        }

        @Test
        @DisplayName("예외(RecruitmentNotFoundException): 존재하지 않는 봉사 모집글")
        void exceptionWhenRecruitmentNotFound() {
            //given
            String newTitle = recruitment.getTitle() + "a";
            LocalDateTime newStartTime = recruitment.getStartTime().plusDays(1);
            LocalDateTime newEndTime = recruitment.getEndTime().plusDays(1);
            LocalDateTime newDeadline = recruitment.getDeadline().plusDays(1);
            int newCapacity = recruitment.getCapacity() + 1;
            String newContent = recruitment.getContent() + "a";
            List<String> newImageUrls = List.of("a1", "a2");

            given(recruitmentRepository.findByShelterIdAndRecruitmentIdWithImages(anyLong(),
                anyLong())).willReturn(Optional.empty());

            //when
            Exception exception = catchException(() -> recruitmentService.updateRecruitment(1L, 1L,
                newTitle, newStartTime, newEndTime, newDeadline, newCapacity, newContent,
                newImageUrls));

            //then
            assertThat(exception).isInstanceOf(RecruitmentNotFoundException.class);
        }
    }
}
