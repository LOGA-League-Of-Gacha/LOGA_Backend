package com.loga.domain.roster.repository;

import com.loga.domain.roster.entity.Roster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 로스터 레포지토리
 */
@Repository
public interface RosterRepository extends MongoRepository<Roster, String>, RosterRepositoryCustom {

    List<Roster> findByUserId(String userId);

    Page<Roster> findByUserId(String userId, Pageable pageable);

    @Query("{ 'communityInfo.isPublic': true }")
    Page<Roster> findPublicRosters(Pageable pageable);

    @Query("{ 'communityInfo.isPublic': true, 'championshipMatch.isChampionshipRoster': true }")
    Page<Roster> findPublicChampionshipRosters(Pageable pageable);

    long countByUserId(String userId);

    @Query(value = "{ 'userId': ?0, 'championshipMatch.isChampionshipRoster': true }", count = true)
    long countChampionshipRostersByUserId(String userId);
}
