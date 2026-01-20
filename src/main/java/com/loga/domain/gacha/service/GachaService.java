package com.loga.domain.gacha.service;

import com.loga.domain.gacha.dto.GachaResultResponse;
import com.loga.domain.player.dto.PlayerResponse;
import com.loga.domain.player.entity.Championship;
import com.loga.domain.player.entity.Player;
import com.loga.domain.player.repository.ChampionshipRepository;
import com.loga.domain.player.repository.PlayerRepository;
import com.loga.domain.user.entity.User;
import com.loga.domain.user.repository.UserRepository;
import com.loga.global.error.BusinessException;
import com.loga.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 가챠 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GachaService {

    private final PlayerRepository playerRepository;
    private final ChampionshipRepository championshipRepository;
    private final UserRepository userRepository;

    /**
     * 특정 포지션에서 랜덤 선수 뽑기
     */
    @Transactional
    public GachaResultResponse drawByPosition(String position, String userId) {
        Player.Position pos = Player.Position.valueOf(position.toUpperCase());

        Player player = playerRepository.findRandomByPosition(pos.name())
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_PLAYERS_AVAILABLE));

        // 뽑힌 횟수 증가
        player.picked();
        playerRepository.save(player);

        // 유저 통계 업데이트
        updateUserGachaStats(userId, 1, false);

        return GachaResultResponse.ofSingle(PlayerResponse.from(player));
    }

    /**
     * 전체 포지션 랜덤 뽑기 (5명)
     */
    @Transactional
    public GachaResultResponse drawFullRoster(String userId) {
        Player top = drawRandomPlayer(Player.Position.TOP);
        Player jungle = drawRandomPlayer(Player.Position.JUNGLE);
        Player mid = drawRandomPlayer(Player.Position.MID);
        Player adc = drawRandomPlayer(Player.Position.ADC);
        Player support = drawRandomPlayer(Player.Position.SUPPORT);

        // 우승 로스터 체크
        Optional<Championship> matched = championshipRepository.findByRosterPlayers(
                top.getId(), jungle.getId(), mid.getId(), adc.getId(), support.getId());

        boolean isChampionship = matched.isPresent();
        String matchedChampionship = matched.map(Championship::getDisplayName).orElse(null);
        Integer matchedYear = matched.map(Championship::getYear).orElse(null);

        // 유저 통계 업데이트
        updateUserGachaStats(userId, 5, isChampionship);

        return GachaResultResponse.ofFullRoster(
                PlayerResponse.from(top),
                PlayerResponse.from(jungle),
                PlayerResponse.from(mid),
                PlayerResponse.from(adc),
                PlayerResponse.from(support),
                isChampionship,
                matchedChampionship,
                matchedYear
        );
    }

    /**
     * 리롤 (멤버십 기능)
     */
    @Transactional
    public GachaResultResponse reroll(String position, String userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!user.canReroll()) {
            throw new BusinessException(ErrorCode.NO_REROLL_LEFT);
        }

        user.useReroll();
        userRepository.save(user);

        return drawByPosition(position, userId);
    }

    private Player drawRandomPlayer(Player.Position position) {
        Player player = playerRepository.findRandomByPosition(position.name())
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_PLAYERS_AVAILABLE));
        player.picked();
        playerRepository.save(player);
        return player;
    }

    private void updateUserGachaStats(String userId, int count, boolean isChampionship) {
        if (userId != null) {
            userRepository.findById(userId).ifPresent(user -> {
                user.incrementGachaCount(count);
                userRepository.save(user);
            });
        }
    }
}
