package com.loga.domain.player.repository;

import com.loga.domain.player.dto.PlayerSearchCondition;
import com.loga.domain.player.entity.Player;
import com.loga.infrastructure.persistence.MongoQueryBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 선수 커스텀 레포지토리 구현체
 * MongoDB Specification 패턴 적용
 */
@Repository
@RequiredArgsConstructor
public class PlayerRepositoryCustomImpl implements PlayerRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Player> search(PlayerSearchCondition condition, Pageable pageable) {
        Query query = MongoQueryBuilder.builder()
                .contains("name", condition.getName())
                .eq("position", condition.getPosition())
                .eq("region", condition.getRegion())
                .eq("currentTeam", condition.getTeam())
                .eq("isActive", condition.getIsActive())
                .addIf(condition.getHasChampionship() != null && condition.getHasChampionship(),
                        builder -> builder.exists("championships", true))
                .build();

        long total = mongoTemplate.count(query, Player.class);

        query.with(pageable);
        List<Player> content = mongoTemplate.find(query, Player.class);

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Player> findTopByPositionOrderByPickedCount(Player.Position position, int limit) {
        Query query = MongoQueryBuilder.builder()
                .eq("position", position)
                .build();

        query.with(Sort.by(Sort.Direction.DESC, "pickedCount"));
        query.limit(limit);

        return mongoTemplate.find(query, Player.class);
    }
}
