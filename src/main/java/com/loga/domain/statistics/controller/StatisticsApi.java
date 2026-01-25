package com.loga.domain.statistics.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.loga.domain.player.dto.PlayerResponse;
import com.loga.domain.statistics.dto.StatisticsResponse;
import com.loga.global.common.dto.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 통계 API 인터페이스 - Swagger 문서화
 */
@Tag(name = "Statistics", description = "서비스 전체 통계 및 인기 선수 API")
public interface StatisticsApi {

    @Operation(summary = "전체 통계 조회", description = "서비스 전체 통계 정보를 조회합니다. 총 사용자 수, 로스터 수, 선수 수와 인기 선수 정보를 포함합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "totalUsers": 1523,
                        "totalRosters": 4892,
                        "totalPlayers": 490,
                        "topPickedPlayers": [
                          {
                            "id": "6972edb355a6e8f16d8e8d60",
                            "name": "Faker",
                            "realName": "이상혁",
                            "position": "MID",
                            "year": 2023,
                            "teamShort": "T1",
                            "teamFull": "T1",
                            "region": "LCK",
                            "nationality": "South Korea",
                            "iso": "kr",
                            "winner": true,
                            "championshipLeague": "WORLDS",
                            "championshipYear": 2023,
                            "pickedCount": 8923,
                            "active": true
                          }
                        ],
                        "topByPosition": {
                          "TOP": {
                            "id": "6972eda655a6e8f16d8e8d29",
                            "name": "Zeus",
                            "position": "TOP",
                            "pickedCount": 5621
                          },
                          "JUNGLE": {
                            "id": "6972eda655a6e8f16d8e8d43",
                            "name": "Canyon",
                            "position": "JUNGLE",
                            "pickedCount": 5102
                          },
                          "MID": {
                            "id": "6972edb355a6e8f16d8e8d60",
                            "name": "Faker",
                            "position": "MID",
                            "pickedCount": 8923
                          },
                          "ADC": {
                            "id": "6972edb355a6e8f16d8e8d7d",
                            "name": "Ruler",
                            "position": "ADC",
                            "pickedCount": 4832
                          },
                          "SUPPORT": {
                            "id": "6972edbc55a6e8f16d8e8d9c",
                            "name": "Keria",
                            "position": "SUPPORT",
                            "pickedCount": 5211
                          }
                        }
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<StatisticsResponse>> getOverallStatistics();

    @Operation(summary = "인기 선수 TOP 10 조회", description = "가챠에서 가장 많이 뽑힌 인기 선수 10명을 조회합니다. 픽 카운트 기준 내림차순 정렬됩니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": [
                        {
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
                          "pickedCount": 8923,
                          "active": true
                        },
                        {
                          "id": "6972eda655a6e8f16d8e8d29",
                          "name": "Zeus",
                          "realName": "최우제",
                          "position": "TOP",
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
                          "pickedCount": 5621,
                          "active": true
                        }
                      ],
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<List<PlayerResponse>>> getTopPickedPlayers();

    @Operation(summary = "포지션별 최고 인기 선수 조회", description = "각 포지션(TOP, JUNGLE, MID, ADC, SUPPORT)에서 가장 많이 뽑힌 선수 1명씩을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "TOP": {
                          "id": "6972eda655a6e8f16d8e8d29",
                          "name": "Zeus",
                          "realName": "최우제",
                          "position": "TOP",
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
                          "pickedCount": 5621,
                          "active": true
                        },
                        "JUNGLE": {
                          "id": "6972eda655a6e8f16d8e8d43",
                          "name": "Canyon",
                          "realName": "김건부",
                          "position": "JUNGLE",
                          "year": 2020,
                          "teamShort": "DK",
                          "teamFull": "DAMWON Gaming",
                          "teamColor": "#0067B1",
                          "region": "LCK",
                          "nationality": "South Korea",
                          "iso": "kr",
                          "winner": true,
                          "championshipLeague": "WORLDS",
                          "championshipYear": 2020,
                          "profileImage": "",
                          "pickedCount": 5102,
                          "active": true
                        },
                        "MID": {
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
                          "pickedCount": 8923,
                          "active": true
                        },
                        "ADC": {
                          "id": "6972edb355a6e8f16d8e8d7d",
                          "name": "Ruler",
                          "realName": "박재혁",
                          "position": "ADC",
                          "year": 2017,
                          "teamShort": "SSG",
                          "teamFull": "Samsung Galaxy",
                          "teamColor": "#1428A0",
                          "region": "LCK",
                          "nationality": "South Korea",
                          "iso": "kr",
                          "winner": true,
                          "championshipLeague": "WORLDS",
                          "championshipYear": 2017,
                          "profileImage": "",
                          "pickedCount": 4832,
                          "active": true
                        },
                        "SUPPORT": {
                          "id": "6972edbc55a6e8f16d8e8d9c",
                          "name": "Keria",
                          "realName": "류민석",
                          "position": "SUPPORT",
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
                          "pickedCount": 5211,
                          "active": true
                        }
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<Map<String, PlayerResponse>>> getTopPlayerByPosition();
}
