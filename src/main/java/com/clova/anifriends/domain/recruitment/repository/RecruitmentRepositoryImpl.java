package com.clova.anifriends.domain.recruitment.repository;

import static com.clova.anifriends.domain.recruitment.QRecruitment.recruitment;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecruitmentRepositoryImpl implements RecruitmentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Recruitment> findRecruitmentsByShelterOrderByCreatedAt(long shelterId,
        String keyword, LocalDate startDate, LocalDate endDate, Boolean content, Boolean title,
        Pageable pageable) {

        Predicate predicate = recruitment.shelter.shelterId.eq(shelterId)
            .and(getDateCondition(startDate, endDate))
            .and(getKeywordCondition(keyword, content, title));

        List<Recruitment> recruitments = queryFactory.selectFrom(recruitment)
            .where(predicate)
            .orderBy(recruitment.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        return new PageImpl<>(recruitments);
    }

    @Override
    public Page<Recruitment> findRecruitmentsByShelterId(long shelterId, Pageable pageable) {

        List<Recruitment> recruitments = queryFactory.selectFrom(recruitment)
            .where(recruitment.shelter.shelterId.eq(shelterId)
                .and(recruitment.info.isClosed.eq(false)))
            .orderBy(recruitment.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        return new PageImpl<>(recruitments);
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
        BooleanExpression predicate = recruitment.isNotNull();
        if (keyword == null || keyword.isBlank()) {
            return predicate;
        }
        if (content) {
            predicate = predicate.or(recruitment.content.content.contains(keyword));
        }
        if (title) {
            predicate = predicate.or(recruitment.title.title.contains(keyword));
        }
        return predicate;
    }
}
