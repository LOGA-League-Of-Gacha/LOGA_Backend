package com.loga.domain.statistics.service;

import com.loga.domain.player.dto.PlayerResponse;
import com.loga.domain.player.entity.Player;
import com.loga.domain.player.repository.PlayerRepository;
import com.loga.domain.roster.repository.RosterRepository;
import com.loga.domain.statistics.dto.StatisticsResponse;
import com.loga.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final PlayerRepository playerRepository;
    private final RosterRepository rosterRepository;
    private final UserRepository userRepository;

    public StatisticsResponse getOverallStatistics() {
        return StatisticsResponse.builder()
                .totalUsers(userRepository.count())
                .totalRosters(rosterRepository.count())
                .totalPlayers(playerRepository.count())
                .topPickedPlayers(getTopPickedPlayers())
                .topByPosition(getTopPlayerByPosition())
                .build();
    }

    public List<PlayerResponse> getTopPickedPlayers() {
        return playerRepository.findTop10ByOrderByPickedCountDesc().stream()
                .map(PlayerResponse::from)
                .toList();
    }

    public Map<String, PlayerResponse> getTopPlayerByPosition() {
        return Arrays.stream(Player.Position.values())
                .collect(Collectors.toMap(
                        Player.Position::name,
                        position -> playerRepository.findByPosition(position).stream()
                                .max((p1, p2) -> Integer.compare(p1.getPickedCount(), p2.getPickedCount()))
                                .map(PlayerResponse::from)
                                .orElse(null)
                ));
    }
}
