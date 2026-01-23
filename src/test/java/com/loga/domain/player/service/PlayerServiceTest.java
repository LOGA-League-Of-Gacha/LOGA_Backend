package com.loga.domain.player.service;

import com.loga.domain.player.dto.PlayerResponse;
import com.loga.domain.player.entity.Player;
import com.loga.domain.player.repository.PlayerRepository;
import com.loga.global.error.BusinessException;
import com.loga.global.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Test
    @DisplayName("전체 선수 목록 조회 성공")
    void getAllPlayers_Success() {
        // given
        Player player1 = Player.builder()
                .name("Faker")
                .realName("Lee Sang-hyeok")
                .position(Player.Position.MID)
                .year(2013)
                .teamShort("SKT")
                .teamFull("SK Telecom T1")
                .teamColor("#FF0000")
                .region("LCK")
                .nationality("KR")
                .iso("kr")
                .isWinner(true)
                .championshipLeague("Worlds")
                .championshipYear(2013)
                .isActive(true)
                .build();
        player1.setId("faker_2013");

        Player player2 = Player.builder()
                .name("Bang")
                .realName("Bae Jun-sik")
                .position(Player.Position.ADC)
                .year(2015)
                .teamShort("SKT")
                .teamFull("SK Telecom T1")
                .teamColor("#FF0000")
                .region("LCK")
                .nationality("KR")
                .iso("kr")
                .isWinner(true)
                .championshipLeague("Worlds")
                .championshipYear(2015)
                .isActive(true)
                .build();
        player2.setId("bang_2015");

        given(playerRepository.findAll()).willReturn(List.of(player1, player2));

        // when
        List<PlayerResponse> result = playerService.getAllPlayers();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("faker_2013");
        verify(playerRepository).findAll();
    }

    @Test
    @DisplayName("선수 상세 조회 성공")
    void getPlayerById_Success() {
        // given
        String playerId = "faker_2013";
        Player player = Player.builder()
                .name("Faker")
                .realName("Lee Sang-hyeok")
                .position(Player.Position.MID)
                .year(2013)
                .teamShort("SKT")
                .teamFull("SK Telecom T1")
                .teamColor("#FF0000")
                .region("LCK")
                .nationality("KR")
                .iso("kr")
                .isWinner(true)
                .championshipLeague("Worlds")
                .championshipYear(2013)
                .isActive(true)
                .build();
        player.setId(playerId);

        given(playerRepository.findById(playerId)).willReturn(Optional.of(player));

        // when
        PlayerResponse result = playerService.getPlayerById(playerId);

        // then
        assertThat(result.getId()).isEqualTo(playerId);
        assertThat(result.getName()).isEqualTo("Faker");
        verify(playerRepository).findById(playerId);
    }

    @Test
    @DisplayName("선수 상세 조회 실패 - 존재하지 않는 선수")
    void getPlayerById_NotFound() {
        // given
        String playerId = "non_existent_player";
        given(playerRepository.findById(playerId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> playerService.getPlayerById(playerId))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PLAYER_NOT_FOUND);
    }

    @Test
    @DisplayName("팀별 선수 조회 성공")
    void getPlayersByTeam_Success() {
        // given
        String team = "SKT";
        Player player = Player.builder()
                .name("Faker")
                .realName("Lee Sang-hyeok")
                .position(Player.Position.MID)
                .year(2013)
                .teamShort("SKT")
                .teamFull("SK Telecom T1")
                .teamColor("#FF0000")
                .region("LCK")
                .nationality("KR")
                .iso("kr")
                .isWinner(true)
                .championshipLeague("Worlds")
                .championshipYear(2013)
                .isActive(true)
                .build();
        player.setId("faker_2013");

        given(playerRepository.findByTeamShort(team)).willReturn(List.of(player));

        // when
        List<PlayerResponse> result = playerService.getPlayersByTeam(team);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTeamShort()).isEqualTo("SKT");
        verify(playerRepository).findByTeamShort(team);
    }
}
