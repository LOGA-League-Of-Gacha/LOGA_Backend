package com.loga.domain.player.repository;

import com.loga.domain.player.entity.Championship;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 우승 로스터 레포지토리
 */
@Repository
public interface ChampionshipRepository extends MongoRepository<Championship, String> {

    List<Championship> findByTournament(String tournament);

    List<Championship> findByYear(int year);

    List<Championship> findByTeam(String team);

    Optional<Championship> findByTournamentAndYear(String tournament, int year);

    /**
     * 로스터가 우승 로스터와 일치하는지 확인
     */
    @Query("{ 'players.topPlayerId': ?0, 'players.junglePlayerId': ?1, 'players.midPlayerId': ?2, 'players.adcPlayerId': ?3, 'players.supportPlayerId': ?4 }")
    Optional<Championship> findByRosterPlayers(String topId, String jungleId, String midId, String adcId, String supportId);
}
