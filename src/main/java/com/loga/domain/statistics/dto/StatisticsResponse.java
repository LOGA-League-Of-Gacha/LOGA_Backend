package com.loga.domain.statistics.dto;

import com.loga.domain.player.dto.PlayerResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class StatisticsResponse {
    private long totalUsers;
    private long totalRosters;
    private long totalPlayers;
    private List<PlayerResponse> topPickedPlayers;
    private Map<String, PlayerResponse> topByPosition;
}
