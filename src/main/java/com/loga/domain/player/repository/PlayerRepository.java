package com.loga.domain.player.repository;

import com.loga.domain.player.entity.Player;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 선수 레포지토리
 */
@Repository
public interface PlayerRepository extends MongoRepository<Player, String>, PlayerRepositoryCustom {

    List<Player> findByPosition(Player.Position position);

    List<Player> findByRegion(String region);

    List<Player> findByCurrentTeam(String team);

    List<Player> findByIsActiveTrue();

    Optional<Player> findByName(String name);

    List<Player> findByNameContainingIgnoreCase(String name);

    List<Player> findTop10ByOrderByPickedCountDesc();

    @Aggregation(pipeline = {
        "{ $match: { position: ?0 } }",
        "{ $sample: { size: 1 } }"
    })
    Optional<Player> findRandomByPosition(String position);

    @Aggregation(pipeline = {
        "{ $sample: { size: ?0 } }"
    })
    List<Player> findRandomPlayers(int count);
}
