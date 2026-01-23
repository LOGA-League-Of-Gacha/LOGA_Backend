package com.loga.domain.gacha.service;

import com.loga.domain.gacha.dto.GachaResultResponse;

import com.loga.domain.player.entity.Player;
import com.loga.domain.player.repository.ChampionshipRepository;
import com.loga.domain.player.repository.PlayerRepository;
import com.loga.domain.user.entity.User;
import com.loga.domain.user.repository.UserRepository;
import com.loga.global.error.BusinessException;
import com.loga.global.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GachaServiceTest {

    @InjectMocks
    private GachaService gachaService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ChampionshipRepository championshipRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("단일 포지션 뽑기 성공 - 비로그인")
    void drawByPosition_Success_NoAuth() {
        // given
        String position = "MID";
        Player player = Player.builder()
                .name("Faker")
                .position(Player.Position.MID)
                .build();
        player.setId("faker_2013");

        given(playerRepository.findRandomByPosition(position)).willReturn(Optional.of(player));

        // when
        GachaResultResponse result = gachaService.drawByPosition(position, null);

        // then
        assertThat(result.getPlayer()).isNotNull();
        assertThat(result.getPlayer().getName()).isEqualTo("Faker");
        verify(playerRepository).findRandomByPosition(position);
        verify(playerRepository).save(player); // picked count update
        verify(userRepository, times(0)).findById(any());
    }

    @Test
    @DisplayName("단일 포지션 뽑기 성공 - 로그인 사용자")
    void drawByPosition_Success_Auth() {
        // given
        String position = "MID";
        String userId = "user1";
        Player player = Player.builder()
                .name("Faker")
                .position(Player.Position.MID)
                .build();
        player.setId("faker_2013");
        User mockUser = mock(User.class);

        given(playerRepository.findRandomByPosition(position)).willReturn(Optional.of(player));
        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));

        // when
        GachaResultResponse result = gachaService.drawByPosition(position, userId);

        // then
        assertThat(result.getPlayer()).isNotNull();
        verify(userRepository).findById(userId);
        verify(mockUser).incrementGachaCount(1);
    }

    @Test
    @DisplayName("전체 로스터 뽑기 성공 - 우승 로스터 아닌 경우")
    void drawFullRoster_Success_NoChampionship() {
        // given
        Player top = Player.builder().position(Player.Position.TOP).build();
        top.setId("top");
        Player jgl = Player.builder().position(Player.Position.JUNGLE).build();
        jgl.setId("jgl");
        Player mid = Player.builder().position(Player.Position.MID).build();
        mid.setId("mid");
        Player adc = Player.builder().position(Player.Position.ADC).build();
        adc.setId("adc");
        Player sup = Player.builder().position(Player.Position.SUPPORT).build();
        sup.setId("sup");

        given(playerRepository.findRandomByPosition("TOP")).willReturn(Optional.of(top));
        given(playerRepository.findRandomByPosition("JUNGLE")).willReturn(Optional.of(jgl));
        given(playerRepository.findRandomByPosition("MID")).willReturn(Optional.of(mid));
        given(playerRepository.findRandomByPosition("ADC")).willReturn(Optional.of(adc));
        given(playerRepository.findRandomByPosition("SUPPORT")).willReturn(Optional.of(sup));

        given(championshipRepository.findByRosterPlayers(any(), any(), any(), any(), any()))
                .willReturn(Optional.empty());

        // when
        GachaResultResponse result = gachaService.drawFullRoster(null);

        // then
        assertThat(result.getTop()).isNotNull();
        assertThat(result.getJungle()).isNotNull();
        assertThat(result.getMid()).isNotNull();
        assertThat(result.getAdc()).isNotNull();
        assertThat(result.getSupport()).isNotNull();
        assertThat(result.isChampionshipRoster()).isFalse();
        verify(playerRepository, times(5)).save(any(Player.class)); // 5 players picked
    }

    @Test
    @DisplayName("리롤 실패 - 리롤 횟수 부족")
    void reroll_Fail_NoRerollLeft() {
        // given
        String userId = "user1";
        User mockUser = mock(User.class);
        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
        given(mockUser.canReroll()).willReturn(false);

        // when & then
        assertThatThrownBy(() -> gachaService.reroll("TOP", userId))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NO_REROLL_LEFT);
    }
}
