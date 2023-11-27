package com.clova.anifriends.domain.recruitment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse.FindRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class RecruitmentCacheRepositoryTest extends BaseIntegrationTest {

    private static final String RECRUITMENT_KEY = "recruitment";
    private static final int ZERO = 0;
    private static final int ALL_ELEMENT = -1;

    @Autowired
    RecruitmentCacheRepository recruitmentCacheRepository;

    @Autowired
    RedisTemplate<String, FindRecruitmentResponse> redisTemplate;

    @AfterEach
    void tearDown() {
        redisTemplate.opsForZSet()
            .removeRange(RECRUITMENT_KEY, ZERO, ALL_ELEMENT);
    }

    @Nested
    @DisplayName("pushNewRecruitment 메서드 호출 시")
    class PushNewRecruitment {

        Shelter shelter;

        @BeforeEach
        void setUp() {
            shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);
        }

        @Test
        @DisplayName("성공: 새로운 Recruitment 캐싱")
        void registerRecruitment() {
            //given
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            recruitmentRepository.save(recruitment);

            //when
            recruitmentCacheRepository.save(recruitment);

            //then
            PageRequest pageRequest = PageRequest.of(0, 20);
            Slice<FindRecruitmentResponse> recruitments = recruitmentCacheRepository.findAll(
                pageRequest);
            List<FindRecruitmentResponse> content = recruitments.getContent();
            assertThat(content).hasSize(1);
            FindRecruitmentResponse recruitmentResponse = content.get(0);
            assertThat(recruitmentResponse.recruitmentId())
                .isEqualTo(recruitment.getRecruitmentId());
        }

        @Test
        @DisplayName("성공: 새로운 Recruitment 100개 생성, 30개 캐싱")
        void overflowTest() {
            //given
            List<Recruitment> recruitments = IntStream.range(0, 100)
                .mapToObj(i -> RecruitmentFixture.recruitment(shelter))
                .toList();
            recruitmentRepository.saveAll(recruitments);
            LocalDateTime now = LocalDateTime.now();
            int hour = 0;
            for (Recruitment recruitment : recruitments) {
                ReflectionTestUtils.setField(recruitment, "createdAt", now.plusHours(hour++));
            }

            //when
            for (Recruitment recruitment : recruitments) {
                recruitmentCacheRepository.save(recruitment);
            }

            //then
            List<Long> recruitmentIdsDesc = recruitments.stream()
                .map(Recruitment::getRecruitmentId)
                .filter(i -> i > 70)
                .sorted(Comparator.reverseOrder())
                .toList();

            Set<FindRecruitmentResponse> findRecruitments = redisTemplate.opsForZSet()
                .reverseRange(RECRUITMENT_KEY, 0, -1);
            assertThat(findRecruitments).hasSize(30);
            assertThat(findRecruitments).map(FindRecruitmentResponse::recruitmentId)
                .containsExactlyElementsOf(recruitmentIdsDesc);


        }
    }

    @Nested
    @DisplayName("findAll 메서드 호출 시")
    class FindAllTest {

        Shelter shelter;

        @BeforeEach
        void setUp() {
            shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);
        }

        @Test
        @DisplayName("성공: 캐싱된 목록이 없을 시 빈 리스트 반환")
        void findAllWhenEmpty() {
            //given
            PageRequest pageRequest = PageRequest.of(0, 20);

            //when
            Slice<FindRecruitmentResponse> recruitments = recruitmentCacheRepository.findAll(pageRequest);

            //then
            assertThat(recruitments.getContent())
                .isEmpty();
        }

        @Test
        @DisplayName("성공: 캐싱된 목록이 있을 시 캐싱된 리스트 반환")
        void findAllWhenCached() {
            //given
            List<Recruitment> recruitments = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            IntStream.range(0, 100)
                .forEach(i -> {
                    Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
                    recruitments.add(recruitment);
                });
            recruitmentRepository.saveAll(recruitments);
            int hour = 0;
            for (Recruitment recruitment : recruitments) {
                ReflectionTestUtils.setField(recruitment, "createdAt", now.plusHours(hour++));
                recruitmentCacheRepository.save(recruitment);
            }
            PageRequest pageRequest = PageRequest.of(0, 20);

            //when
            Slice<FindRecruitmentResponse> cachedRecruitments
                = recruitmentCacheRepository.findAll(pageRequest);

            //then
            List<Long> recruitmentIdsDesc = recruitments.stream()
                .map(Recruitment::getRecruitmentId)
                .filter(i -> i > 80)
                .sorted(Comparator.reverseOrder())
                .toList();

            assertThat(cachedRecruitments.getContent())
                .hasSize(20)
                .map(FindRecruitmentResponse::recruitmentId)
                .containsExactlyElementsOf(recruitmentIdsDesc);
        }

        @Test
        @DisplayName("성공: 캐싱된 목록은 최대 20개까지만 조회 가능")
        void findAllMax20() {
            //given
            int pageSize = 30;
            PageRequest pageRequest = PageRequest.of(0, pageSize);
            List<Recruitment> recruitments = RecruitmentFixture.recruitments(shelter, 100);
            recruitmentRepository.saveAll(recruitments);
            int hour = 0;
            LocalDateTime now = LocalDateTime.now();
            for (Recruitment recruitment : recruitments) {
                ReflectionTestUtils.setField(recruitment, "createdAt", now.plusHours(hour++));
                recruitmentCacheRepository.save(recruitment);
            }

            //when
            Slice<FindRecruitmentResponse> cachedRecruitments = recruitmentCacheRepository.findAll(
                pageRequest);

            //then
            assertThat(cachedRecruitments.getContent())
                .hasSizeLessThan(pageSize)
                .hasSize(20);
            assertThat(cachedRecruitments.hasNext()).isTrue();
        }

        @Test
        @DisplayName("성공: 캐싱된 목록이 요청 개수 이하인 경우 hasNext는 false")
        void findAllWhenLTRequestPageSizeThenHasNextIsFalse() {
            //given
            int pageSize = 10;
            PageRequest pageRequest = PageRequest.of(0, pageSize);
            List<Recruitment> recruitments = RecruitmentFixture.recruitments(shelter, 10);
            recruitmentRepository.saveAll(recruitments);
            int hour = 0;
            LocalDateTime now = LocalDateTime.now();
            for (Recruitment recruitment : recruitments) {
                ReflectionTestUtils.setField(recruitment, "createdAt", now.plusHours(hour++));
                recruitmentCacheRepository.save(recruitment);
            }

            //when
            Slice<FindRecruitmentResponse> cachedRecruitments = recruitmentCacheRepository.findAll(
                pageRequest);

            //then
            assertThat(cachedRecruitments.hasNext()).isFalse();
        }
    }

    @Nested
    @DisplayName("updateCachedRecruitments 메서드 호출 시")
    class UpdateCachedRecruitmentsTest {

        Shelter shelter;
        List<Recruitment> recruitments;

        @BeforeEach
        void setUp() {
            shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);
            recruitments = RecruitmentFixture.recruitments(shelter, 40);
            recruitmentRepository.saveAll(recruitments);
        }

        @Test
        @DisplayName("성공: 캐시된 Recruitment 업데이트 됨")
        void updateCachedRecruitment() {
            //given
            int hour = 0;
            LocalDateTime now = LocalDateTime.now();
            for (Recruitment recruitment : recruitments) {
                ReflectionTestUtils.setField(recruitment, "createdAt", now.plusHours(hour++));
                recruitmentCacheRepository.save(recruitment);
            }
            Recruitment needToUpdateRecruitment = recruitments.get(20);
            FindRecruitmentResponse oldCachedRecruitment
                = FindRecruitmentResponse.from(needToUpdateRecruitment);
            needToUpdateRecruitment.updateRecruitment("update", null, null, null, null, null, null);

            //when
            recruitmentCacheRepository.update(needToUpdateRecruitment);

            //then
            PageRequest pageRequest = PageRequest.of(0, 20);
            Slice<FindRecruitmentResponse> cachedRecruitments = recruitmentCacheRepository.findAll(
                pageRequest);
            FindRecruitmentResponse newCachedRecruitment
                = FindRecruitmentResponse.from(needToUpdateRecruitment);
            assertThat(cachedRecruitments.getContent())
                .contains(newCachedRecruitment)
                .doesNotContain(oldCachedRecruitment);
        }

        @Test
        @DisplayName("성공: 캐시된 Recruitment 없는 경우 무시됨")
        void ignoreIfCachedRecruitmentDoesNotExists() {
            //given
            int hour = 0;
            LocalDateTime now = LocalDateTime.now();
            for (Recruitment recruitment : recruitments) {
                ReflectionTestUtils.setField(recruitment, "createdAt", now.plusHours(hour++));
                recruitmentCacheRepository.save(recruitment);
            }
            Recruitment needToUpdateRecruitment = recruitments.get(0);
            needToUpdateRecruitment.updateRecruitment("update", null, null, null, null, null, null);

            //when
            recruitmentCacheRepository.update(needToUpdateRecruitment);

            //then
            long createdAtScore
                = needToUpdateRecruitment.getCreatedAt().toEpochSecond(ZoneOffset.UTC);
            ZSetOperations<String, FindRecruitmentResponse> cachedRecruitments
                = redisTemplate.opsForZSet();
            Set<FindRecruitmentResponse> recruitments = cachedRecruitments.rangeByScore(
                RECRUITMENT_KEY, createdAtScore, createdAtScore);
            assertThat(recruitments).isEmpty();
        }
    }

    @Nested
    @DisplayName("deleteCachedRecruitment 메서드 실행 시")
    class DeleteCachedRecruitmentTest {

        Shelter shelter;

        @BeforeEach
        void setUp() {
            shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);
        }

        @Test
        @DisplayName("성공: 캐시된 Recruitment 삭제 됨")
        void deleteCachedRecruitment() {
            //given
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            recruitmentRepository.save(recruitment);
            recruitmentCacheRepository.save(recruitment);

            //when
            recruitmentCacheRepository.delete(recruitment);

            //then
            ZSetOperations<String, FindRecruitmentResponse> cachedRecruitments
                = redisTemplate.opsForZSet();
            long createdAtScore = recruitment.getCreatedAt().toEpochSecond(ZoneOffset.UTC);
            Set<FindRecruitmentResponse> recruitments = cachedRecruitments.rangeByScore(
                RECRUITMENT_KEY, createdAtScore, createdAtScore);
            assertThat(recruitments).isEmpty();
        }

        @Test
        @DisplayName("성공: 캐시된 Recruitment 없는 경우 무시 됨")
        void ignoreWhenCachedRecruitmentDoesNotExists() {
            //given
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            recruitmentRepository.save(recruitment);

            //when
            recruitmentCacheRepository.delete(recruitment);

            //then
            ZSetOperations<String, FindRecruitmentResponse> cachedRecruitments
                = redisTemplate.opsForZSet();
            long createdAtScore = recruitment.getCreatedAt().toEpochSecond(ZoneOffset.UTC);
            Set<FindRecruitmentResponse> recruitments = cachedRecruitments.rangeByScore(
                RECRUITMENT_KEY, createdAtScore, createdAtScore);
            assertThat(recruitments).isEmpty();
        }
    }
}
