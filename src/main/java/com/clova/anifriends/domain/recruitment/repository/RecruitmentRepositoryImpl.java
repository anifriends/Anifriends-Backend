package com.clova.anifriends.domain.recruitment.repository;

import static com.clova.anifriends.domain.recruitment.QRecruitment.recruitment;
import static com.clova.anifriends.domain.shelter.QShelter.shelter;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.controller.KeywordCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecruitmentRepositoryImpl implements
    RecruitmentRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Page<Recruitment> findRecruitments(String keyword, LocalDate startDate,
        LocalDate endDate, Boolean isClosed, KeywordCondition keywordCondition, Pageable pageable) {
        List<Recruitment> content = query.select(recruitment)
            .from(recruitment)
            .join(recruitment.shelter).fetchJoin()
            .leftJoin(shelter.image).fetchJoin()
            .where(
                keywordSearch(keyword, keywordCondition),
                recruitmentIsClosed(isClosed),
                recruitmentStartTimeGoe(startDate),
                recruitmentStartTimeLoe(endDate)
            )
            .orderBy(recruitment.createdAt.desc())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        Long count = query.select(recruitment.count())
            .from(recruitment)
            .join(recruitment.shelter)
            .where(
                keywordSearch(keyword, keywordCondition),
                recruitmentIsClosed(isClosed),
                recruitmentStartTimeGoe(startDate),
                recruitmentStartTimeLoe(endDate)
            ).fetchOne();
        return new PageImpl<>(content, pageable, count != null ? count : 0);
    }

    @Override
    public Slice<Recruitment> findRecruitmentsV2(String keyword, LocalDate startDate,
        LocalDate endDate, Boolean isClosed, KeywordCondition keywordCondition,
        LocalDateTime createdAt, Long recruitmentId,
        Pageable pageable) {
        List<Recruitment> content = query.select(recruitment)
            .from(recruitment)
            .join(recruitment.shelter).fetchJoin()
            .leftJoin(shelter.image).fetchJoin()
            .where(
                keywordSearch(keyword, keywordCondition),
                recruitmentIsClosed(isClosed),
                recruitmentStartTimeGoe(startDate),
                recruitmentStartTimeLoe(endDate),
                cursorId(recruitmentId, createdAt)
            )
            .orderBy(recruitment.createdAt.desc())
            .limit(pageable.getPageSize() + 1L)
            .offset(pageable.getOffset())
            .fetch();

        boolean hasNext = hasNext(pageable.getPageSize(), content);
        return new SliceImpl<>(content, pageable, hasNext);
    }

    private boolean hasNext(int pageSize, List<Recruitment> recruitments) {
        if (recruitments.size() <= pageSize) {
            return false;
        }

        recruitments.remove(pageSize);
        return true;
    }

    @Override
    public Long countFindRecruitmentsV2(String keyword, LocalDate startDate,
        LocalDate endDate, Boolean isClosed, KeywordCondition keywordCondition) {

        Long count = query.select(recruitment.count())
            .from(recruitment)
            .join(recruitment.shelter)
            .where(
                keywordSearch(keyword, keywordCondition),
                recruitmentIsClosed(isClosed),
                recruitmentStartTimeGoe(startDate),
                recruitmentStartTimeLoe(endDate)
            ).fetchOne();

        return count != null ? count : 0;
    }

    private BooleanExpression cursorId(Long recruitmentId, LocalDateTime createdAt) {
        if (recruitmentId == null || createdAt == null) {
            return null;
        }

        return recruitment.createdAt.lt(createdAt)
            .or(
                recruitment.recruitmentId.lt(recruitmentId)
                    .and(recruitment.createdAt.eq(createdAt)
                    )
            );
    }

    private BooleanBuilder keywordSearch(String keyword, KeywordCondition keywordCondition) {
        return nullSafeBuilder(
            () -> recruitmentTitleContains(keyword, keywordCondition.titleFilter()))
            .or(
                nullSafeBuilder(
                () -> recruitmentContentContains(keyword, keywordCondition.contentFilter())))
            .or(nullSafeBuilder(
                () -> recruitmentShelterNameContains(keyword, keywordCondition.shelterNameFilter())));
    }

    private BooleanExpression recruitmentTitleContains(String keyword, boolean titleFilter) {
        if (!titleFilter) {
            return null;
        }
        return keyword != null ? recruitment.title.title.contains(keyword) : null;
    }

    private BooleanExpression recruitmentContentContains(String keyword, boolean contentFilter) {
        if (!contentFilter) {
            return null;
        }
        return keyword != null ? recruitment.content.content.contains(keyword) : null;
    }

    private BooleanExpression recruitmentShelterNameContains(String keyword,
        boolean shelterNameFilter) {
        if (!shelterNameFilter) {
            return null;
        }
        return keyword != null ? recruitment.shelter.name.name.contains(keyword) : null;
    }

    private BooleanExpression recruitmentIsClosed(Boolean isClosed) {
        if (Objects.isNull(isClosed)) {
            return null;
        }
        return recruitment.info.isClosed.eq(isClosed);
    }

    private BooleanExpression recruitmentStartTimeGoe(LocalDate startDate) {
        return startDate != null ? recruitment.info.startTime.goe(startDate.atStartOfDay()) : null;
    }

    private BooleanExpression recruitmentStartTimeLoe(LocalDate endDate) {
        return endDate != null ? recruitment.info.startTime.loe(endDate.plusDays(1).atStartOfDay())
            : null;
    }

    private BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> supplier) {
        try {
            return new BooleanBuilder(supplier.get());
        } catch (IllegalArgumentException e) {
            return new BooleanBuilder();
        }
    }

    @Override
    public Page<Recruitment> findRecruitmentsByShelterOrderByCreatedAt(long shelterId,
        String keyword, LocalDate startDate, LocalDate endDate, Boolean isClosed, Boolean content,
        Boolean title,
        Pageable pageable) {

        Predicate predicate = recruitment.shelter.shelterId.eq(shelterId)
            .and(getDateCondition(startDate, endDate))
            .and(getKeywordCondition(keyword, content, title))
            .and(recruitmentIsClosed(isClosed));

        List<Recruitment> recruitments = query.selectFrom(recruitment)
            .where(predicate)
            .orderBy(recruitment.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = query.select(recruitment.count())
            .from(recruitment)
            .where(predicate)
            .fetchOne();

        return new PageImpl<>(recruitments, pageable, count == null ? 0 : count);
    }

    @Override
    public Page<Recruitment> findShelterRecruitments(long shelterId, Pageable pageable) {

        List<Recruitment> recruitments = query.selectFrom(recruitment)
            .where(recruitment.shelter.shelterId.eq(shelterId)
                .and(recruitment.info.isClosed.eq(false)))
            .orderBy(recruitment.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = query.select(recruitment.count())
            .from(recruitment)
            .where(recruitment.shelter.shelterId.eq(shelterId)
                .and(recruitment.info.isClosed.eq(false)))
            .fetchOne();

        return new PageImpl<>(recruitments, pageable, count == null ? 0 : count);
    }

    Predicate getDateCondition(LocalDate startDate, LocalDate endDate) {
        BooleanExpression predicate = recruitment.isNotNull();
        if (startDate != null) {
            predicate = predicate.and(recruitment.info.startTime.goe(startDate.atStartOfDay()));
        }
        if (endDate != null) {
            predicate = predicate.and(recruitment.info.startTime.loe(endDate.atStartOfDay()));
        }
        return predicate;
    }

    Predicate getKeywordCondition(String keyword, Boolean content, Boolean title) {
        return nullSafeBuilder(() -> title != null ? recruitmentTitleContains(keyword, title) : null)
            .or(nullSafeBuilder(() -> content != null ? recruitmentContentContains(keyword, content) : null));
    }
}
