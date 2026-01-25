package com.loga.domain.gacha.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.loga.domain.gacha.dto.GachaResultResponse;
import com.loga.domain.user.entity.User;
import com.loga.global.common.dto.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 가챠(뽑기) API 인터페이스 - Swagger 문서화
 */
@Tag(name = "Gacha", description = "가챠(뽑기) 시스템 API - 랜덤 선수 뽑기, 로스터 구성")
public interface GachaApi {

    @Operation(summary = "단일 선수 뽑기", description = """
            지정된 포지션(TOP, JUNGLE, MID, ADC, SUPPORT)의 선수 1명을 랜덤하게 뽑습니다.

            - 비로그인 사용자도 뽑기 가능
            - 로그인 사용자는 통계에 기록됨
            """)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "뽑기 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "player": {
                          "id": "6972edb355a6e8f16d8e8d60",
                          "name": "Faker",
                          "realName": "이상혁",
                          "position": "MID",
                          "year": 2023,
                          "teamShort": "T1",
                          "teamFull": "T1",
                          "teamColor": "#E4002B",
                          "region": "LCK",
                          "nationality": "South Korea",
                          "iso": "kr",
                          "winner": true,
                          "championshipLeague": "WORLDS",
                          "championshipYear": 2023,
                          "profileImage": "",
                          "pickedCount": 8924,
                          "active": true
                        },
                        "top": null,
                        "jungle": null,
                        "mid": null,
                        "adc": null,
                        "support": null,
                        "isChampionshipRoster": false,
                        "matchedChampionship": null,
                        "matchedYear": null
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 포지션 값", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": false,
                      "code": "INVALID_POSITION",
                      "message": "Invalid position: INVALID. Must be one of: TOP, JUNGLE, MID, ADC, SUPPORT",
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<GachaResultResponse>> drawByPosition(
            @Parameter(description = "포지션 (TOP, JUNGLE, MID, ADC, SUPPORT)", example = "MID") @PathVariable String position,
            @Parameter(hidden = true) User user);

    @Operation(summary = "전체 로스터 뽑기", description = """
            5개 포지션(TOP, JUNGLE, MID, ADC, SUPPORT) 모두를 한 번에 뽑습니다.

            - 5명의 선수가 동시에 랜덤 선택됨
            - 우승 로스터와 일치하면 isChampionshipRoster가 true로 반환됨
            - 비로그인 사용자도 뽑기 가능
            """)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "전체 로스터 뽑기 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "player": null,
                        "top": {
                          "id": "6972eda655a6e8f16d8e8d29",
                          "name": "Zeus",
                          "realName": "최우제",
                          "position": "TOP",
                          "year": 2023,
                          "teamShort": "T1",
                          "teamFull": "T1",
                          "teamColor": "#E4002B",
                          "region": "LCK",
                          "winner": true,
                          "championshipLeague": "WORLDS",
                          "championshipYear": 2023,
                          "pickedCount": 5622,
                          "active": true
                        },
                        "jungle": {
                          "id": "6972eda655a6e8f16d8e8d43",
                          "name": "Oner",
                          "realName": "문현준",
                          "position": "JUNGLE",
                          "year": 2023,
                          "teamShort": "T1",
                          "winner": true,
                          "championshipYear": 2023
                        },
                        "mid": {
                          "id": "6972edb355a6e8f16d8e8d60",
                          "name": "Faker",
                          "realName": "이상혁",
                          "position": "MID",
                          "year": 2023,
                          "teamShort": "T1",
                          "winner": true,
                          "championshipYear": 2023
                        },
                        "adc": {
                          "id": "6972edb355a6e8f16d8e8d7d",
                          "name": "Gumayusi",
                          "realName": "이민형",
                          "position": "ADC",
                          "year": 2023,
                          "teamShort": "T1",
                          "winner": true,
                          "championshipYear": 2023
                        },
                        "support": {
                          "id": "6972edbc55a6e8f16d8e8d9c",
                          "name": "Keria",
                          "realName": "류민석",
                          "position": "SUPPORT",
                          "year": 2023,
                          "teamShort": "T1",
                          "winner": true,
                          "championshipYear": 2023
                        },
                        "isChampionshipRoster": true,
                        "matchedChampionship": "WORLDS",
                        "matchedYear": 2023
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<GachaResultResponse>> drawFullRoster(
            @Parameter(hidden = true) User user);

    @Operation(summary = "리롤 (Reroll)", description = """
            특정 포지션의 선수를 다시 뽑습니다.

            - 로그인 필요 (선택적)
            - 멤버십에 따라 무료 리롤 횟수 제한
            - 프리미엄 멤버십: 무제한 리롤
            - 일반 사용자: 일일 3회 무료 리롤
            """)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "리롤 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "player": {
                          "id": "6972edb355a6e8f16d8e8d61",
                          "name": "Chovy",
                          "realName": "정지훈",
                          "position": "MID",
                          "year": 2023,
                          "teamShort": "GEN",
                          "teamFull": "Gen.G",
                          "teamColor": "#AA8A00",
                          "region": "LCK",
                          "nationality": "South Korea",
                          "iso": "kr",
                          "winner": false,
                          "championshipLeague": null,
                          "championshipYear": null,
                          "profileImage": "",
                          "pickedCount": 4521,
                          "active": true
                        },
                        "isChampionshipRoster": false,
                        "matchedChampionship": null,
                        "matchedYear": null
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 포지션 또는 리롤 횟수 초과", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": false,
                      "code": "REROLL_LIMIT_EXCEEDED",
                      "message": "Daily reroll limit exceeded. Upgrade to premium for unlimited rerolls.",
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<GachaResultResponse>> reroll(
            @Parameter(description = "리롤할 포지션 (TOP, JUNGLE, MID, ADC, SUPPORT)", example = "MID") @PathVariable String position,
            @Parameter(hidden = true) User user);
}
