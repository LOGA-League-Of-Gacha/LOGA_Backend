package com.loga.domain.roster.service;

import com.loga.domain.player.entity.Championship;
import com.loga.domain.player.entity.Player;
import com.loga.domain.player.repository.ChampionshipRepository;
import com.loga.domain.player.repository.PlayerRepository;
import com.loga.domain.roster.dto.CreateRosterRequest;
import com.loga.domain.roster.dto.RosterResponse;
import com.loga.domain.roster.dto.RosterSearchCondition;
import com.loga.domain.roster.entity.Roster;
import com.loga.domain.roster.repository.RosterRepository;
import com.loga.domain.user.entity.User;
import com.loga.domain.user.repository.UserRepository;
import com.loga.global.common.dto.response.PageResponse;
import com.loga.global.error.BusinessException;
import com.loga.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 로스터 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RosterService {

    private final RosterRepository rosterRepository;
    private final PlayerRepository playerRepository;
    private final ChampionshipRepository championshipRepository;
    private final UserRepository userRepository;

    /**
     * 로스터 생성
     */
    @Transactional
    public RosterResponse createRoster(CreateRosterRequest request, User user) {
        // 선수 정보 조회
        Player top = getPlayer(request.getTopPlayerId());
        Player jungle = getPlayer(request.getJunglePlayerId());
        Player mid = getPlayer(request.getMidPlayerId());
        Player adc = getPlayer(request.getAdcPlayerId());
        Player support = getPlayer(request.getSupportPlayerId());

        // 우승 로스터 체크
        Optional<Championship> matched = championshipRepository.findByRosterPlayers(
                top.getId(), jungle.getId(), mid.getId(), adc.getId(), support.getId());

        Roster.ChampionshipMatch championshipMatch = matched
                .map(c -> Roster.ChampionshipMatch.matched(c.getDisplayName(), c.getYear()))
                .orElse(Roster.ChampionshipMatch.none());

        Roster.RosterPlayers players = Roster.RosterPlayers.builder()
                .topPlayerId(top.getId())
                .junglePlayerId(jungle.getId())
                .midPlayerId(mid.getId())
                .adcPlayerId(adc.getId())
                .supportPlayerId(support.getId())
                .topPlayerName(top.getName())
                .junglePlayerName(jungle.getName())
                .midPlayerName(mid.getName())
                .adcPlayerName(adc.getName())
                .supportPlayerName(support.getName())
                .build();

        Roster roster = Roster.create(
                user.getId(),
                user.getNickname(),
                players,
                championshipMatch,
                request.isPublic(),
                request.getGameMode()
        );

        roster = rosterRepository.save(roster);

        // 유저 통계 업데이트
        user.addRoster(roster.getId(), matched.isPresent());
        userRepository.save(user);

        return RosterResponse.from(roster);
    }

    /**
     * 로스터 상세 조회
     */
    @Transactional(readOnly = true)
    public RosterResponse getRosterById(String id) {
        Roster roster = rosterRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROSTER_NOT_FOUND));
        return RosterResponse.from(roster);
    }

    /**
     * 유저 로스터 목록
     */
    @Transactional(readOnly = true)
    public PageResponse<RosterResponse> getUserRosters(String userId, Pageable pageable) {
        Page<Roster> page = rosterRepository.findByUserId(userId, pageable);
        return PageResponse.from(page, RosterResponse::from);
    }

    /**
     * 동적 검색
     */
    @Transactional(readOnly = true)
    public PageResponse<RosterResponse> searchRosters(RosterSearchCondition condition, Pageable pageable) {
        Page<Roster> page = rosterRepository.search(condition, pageable);
        return PageResponse.from(page, RosterResponse::from);
    }

    /**
     * 좋아요 토글
     */
    @Transactional
    public RosterResponse toggleLike(String rosterId, User user) {
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROSTER_NOT_FOUND));

        roster.toggleLike(user.getId());
        rosterRepository.save(roster);

        return RosterResponse.from(roster);
    }

    /**
     * 로스터 삭제
     */
    @Transactional
    public void deleteRoster(String rosterId, User user) {
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROSTER_NOT_FOUND));

        if (!roster.isOwnedBy(user.getId()) && user.getRole() != User.Role.ADMIN) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        rosterRepository.delete(roster);

        // 유저 통계 업데이트
        user.removeRoster(rosterId);
        userRepository.save(user);
    }

    private Player getPlayer(String playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLAYER_NOT_FOUND));
    }
}
