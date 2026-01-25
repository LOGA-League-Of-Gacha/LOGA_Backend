package com.loga.domain.player.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.loga.domain.player.dto.PlayerResponse;
import com.loga.global.common.dto.response.ApiResponse;
import com.loga.global.common.dto.response.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 선수 API 인터페이스 - Swagger 문서화
 */
@Tag(name = "Player", description = "선수 도감 및 검색 API - LOL 프로 선수 정보 조회")
public interface PlayerApi {

    @Operation(summary = "전체 선수 목록 조회", description = "등록된 모든 선수의 목록을 조회합니다. 490명 이상의 LOL 프로 선수 데이터가 포함됩니다.")
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
    ResponseEntity<ApiResponse<List<PlayerResponse>>> getAllPlayers();

    @Operation(summary = "선수 상세 조회", description = "선수 ID로 특정 선수의 상세 정보를 조회합니다. 시즌 카드 모델로 동일 선수의 여러 시즌 데이터가 별도 ID로 존재합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
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
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "선수를 찾을 수 없음", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": false,
                      "code": "PLAYER_NOT_FOUND",
                      "message": "Player not found",
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<PlayerResponse>> getPlayer(
            @Parameter(description = "선수 ID (MongoDB ObjectId)", example = "6972edb355a6e8f16d8e8d60") @PathVariable String id);

    @Operation(summary = "선수 검색", description = "다양한 조건(이름, 포지션, 지역, 팀, 현역/우승 여부)으로 선수를 검색합니다. 모든 조건은 선택사항이며, AND 조건으로 결합됩니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "items": [
                          {
                            "id": "6972edb355a6e8f16d8e8d60",
                            "name": "Faker",
                            "realName": "이상혁",
                            "position": "MID",
                            "year": 2023,
                            "teamShort": "T1",
                            "region": "LCK",
                            "winner": true,
                            "championshipLeague": "WORLDS",
                            "championshipYear": 2023,
                            "pickedCount": 8923,
                            "active": true
                          }
                        ],
                        "pagination": {
                          "currentPage": 1,
                          "pageSize": 20,
                          "totalItems": 1,
                          "totalPages": 1,
                          "hasNextPage": false,
                          "hasPrevPage": false
                        }
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<PageResponse<PlayerResponse>>> searchPlayers(
            @Parameter(description = "선수 이름 (부분 일치, 대소문자 무시)", example = "faker") @RequestParam(required = false) String name,
            @Parameter(description = "포지션 (TOP, JUNGLE, MID, ADC, SUPPORT)", example = "MID") @RequestParam(required = false) String position,
            @Parameter(description = "지역/리그 (LCK, LPL, LEC, LCS 등)", example = "LCK") @RequestParam(required = false) String region,
            @Parameter(description = "팀 약어 (T1, GEN, DK, SKT 등)", example = "T1") @RequestParam(required = false) String team,
            @Parameter(description = "현역 여부 (true: 현역만, false: 은퇴 선수만)", example = "true") @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "우승 경력 보유 여부 (true: 우승자만)", example = "true") @RequestParam(required = false) Boolean hasChampionship,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기 (기본: 20)", example = "20") @RequestParam(defaultValue = "20") int size);

    @Operation(summary = "포지션별 선수 조회", description = "특정 포지션의 모든 선수를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": [
                        {
                          "id": "6972edb355a6e8f16d8e8d60",
                          "name": "Faker",
                          "position": "MID",
                          "teamShort": "T1",
                          "region": "LCK"
                        },
                        {
                          "id": "6972edb355a6e8f16d8e8d61",
                          "name": "Chovy",
                          "position": "MID",
                          "teamShort": "GEN",
                          "region": "LCK"
                        }
                      ],
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<List<PlayerResponse>>> getPlayersByPosition(
            @Parameter(description = "포지션 (TOP, JUNGLE, MID, ADC, SUPPORT)", example = "MID") @PathVariable String position);

    @Operation(summary = "지역별 선수 조회", description = "특정 지역(리그)의 모든 선수를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<ApiResponse<List<PlayerResponse>>> getPlayersByRegion(
            @Parameter(description = "지역/리그 (LCK, LPL, LEC, LCS 등)", example = "LCK") @PathVariable String region);

    @Operation(summary = "팀별 선수 조회", description = "특정 팀(약어)의 모든 선수를 조회합니다. 같은 팀의 역대 시즌 선수가 모두 포함됩니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<ApiResponse<List<PlayerResponse>>> getPlayersByTeam(
            @Parameter(description = "팀 약어 (T1, SKT, GEN, DK 등)", example = "T1") @PathVariable String team);

    @Operation(summary = "인기 선수 TOP 10", description = "가챠에서 가장 많이 뽑힌 인기 선수 10명을 조회합니다. pickedCount 기준 내림차순 정렬됩니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": [
                        {
                          "id": "6972edb355a6e8f16d8e8d60",
                          "name": "Faker",
                          "position": "MID",
                          "teamShort": "T1",
                          "pickedCount": 8923
                        },
                        {
                          "id": "6972eda655a6e8f16d8e8d29",
                          "name": "Zeus",
                          "position": "TOP",
                          "teamShort": "T1",
                          "pickedCount": 5621
                        }
                      ],
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<List<PlayerResponse>>> getTopPickedPlayers();

    @Operation(summary = "현역 선수 조회", description = "현재 활동 중인(Active) 선수 목록만 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<ApiResponse<List<PlayerResponse>>> getActivePlayers();
}
