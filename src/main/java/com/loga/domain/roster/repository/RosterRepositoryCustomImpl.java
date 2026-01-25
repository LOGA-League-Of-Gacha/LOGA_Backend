package com.loga.domain.roster.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.loga.domain.roster.dto.RosterSearchCondition;
import com.loga.domain.roster.entity.Roster;
import com.loga.infrastructure.persistence.MongoQueryBuilder;

import lombok.RequiredArgsConstructor;

/**
 * 로스터 커스텀 레포지토리 구현체
 */
@Repository
@RequiredArgsConstructor
public class RosterRepositoryCustomImpl implements RosterRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Roster> search(RosterSearchCondition condition, Pageable pageable) {
        Query query = MongoQueryBuilder.builder()
                .eq("userId", condition.getUserId())
                .eq("communityInfo.isPublic", condition.getIsPublic())
                .eq("championshipMatch.isChampionshipRoster", condition.getIsChampionship())
                .eq("gameMode", condition.getGameMode())
                .eq("rankInfo.tier", condition.getTier())
                .build();

        long total = mongoTemplate.count(query, Roster.class);

        query.with(pageable);
        List<Roster> content = mongoTemplate.find(query, Roster.class);

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Roster> findByPopularity(Pageable pageable) {
        Query query = MongoQueryBuilder.builder()
                .eq("communityInfo.isPublic", true)
                .build();

        query.with(Sort.by(Sort.Direction.DESC, "communityInfo.likeCount"));

        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Roster.class);

        query.with(pageable);
        List<Roster> content = mongoTemplate.find(query, Roster.class);

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Roster> findRankedByTier(String tier, Pageable pageable) {
        Query query = MongoQueryBuilder.builder()
                .eq("gameMode", Roster.GameMode.RANKED)
                .eq("rankInfo.tier", tier)
                .build();

        query.with(Sort.by(Sort.Direction.DESC, "rankInfo.score"));

        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Roster.class);

        query.with(pageable);
        List<Roster> content = mongoTemplate.find(query, Roster.class);

        return new PageImpl<>(content, pageable, total);
    }
}
