package com.loga.domain.roster.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.loga.domain.roster.dto.CreateRosterRequest;
import com.loga.domain.roster.dto.RosterResponse;
import com.loga.domain.user.entity.User;
import com.loga.global.common.dto.response.ApiResponse;
import com.loga.global.common.dto.response.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 로스터 API 인터페이스 - Swagger 문서화
 */
@Tag(name = "Roster", description = "로스터(나만의 드림팀) 관리 API")
public interface RosterApi {

    @Operation(summary = "로스터 생성", description = "가챠로 뽑은 선수들로 나만의 로스터를 생성합니다. 5개 포지션 모두 필수입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로스터 생성 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "id": "6789abc123",
                        "userId": "user123",
                        "userName": "Summoner1",
                        "players": {
                          "topPlayerId": "6972eda655a6e8f16d8e8d29",
                          "junglePlayerId": "6972eda655a6e8f16d8e8d43",
                          "midPlayerId": "6972edb355a6e8f16d8e8d60",
                          "adcPlayerId": "6972edb355a6e8f16d8e8d7d",
                          "supportPlayerId": "6972edbc55a6e8f16d8e8d9c",
                          "topPlayerName": "Zeus",
                          "junglePlayerName": "Oner",
                          "midPlayerName": "Faker",
                          "adcPlayerName": "Gumayusi",
                          "supportPlayerName": "Keria"
                        },
                        "championshipMatch": {
                          "isChampionshipRoster": true,
                          "matchedChampionship": "WORLDS",
                          "matchedYear": 2023
                        },
                        "community": {
                          "isPublic": false,
                          "likeCount": 0,
                          "commentCount": 0
                        },
                        "gameMode": "NORMAL",
                        "rank": null,
                        "createdAt": "2026-01-25T14:30:00"
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (필수 필드 누락)", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": false,
                      "code": "VALIDATION_ERROR",
                      "message": "Top player is required",
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": false,
                      "message": "Unauthorized"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<RosterResponse>> createRoster(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "로스터 생성 요청", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateRosterRequest.class), examples = @ExampleObject(value = """
                    {
                      "topPlayerId": "6972eda655a6e8f16d8e8d29",
                      "junglePlayerId": "6972eda655a6e8f16d8e8d43",
                      "midPlayerId": "6972edb355a6e8f16d8e8d60",
                      "adcPlayerId": "6972edb355a6e8f16d8e8d7d",
                      "supportPlayerId": "6972edbc55a6e8f16d8e8d9c",
                      "isPublic": false,
                      "gameMode": "NORMAL"
                    }
                    """))) @RequestBody CreateRosterRequest request,
            @Parameter(hidden = true) User user);

    @Operation(summary = "로스터 상세 조회", description = "로스터 ID로 특정 로스터의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "id": "6789abc123",
                        "userId": "user123",
                        "userName": "Summoner1",
                        "players": {
                          "topPlayerId": "6972eda655a6e8f16d8e8d29",
                          "topPlayerName": "Zeus",
                          "junglePlayerId": "6972eda655a6e8f16d8e8d43",
                          "junglePlayerName": "Oner",
                          "midPlayerId": "6972edb355a6e8f16d8e8d60",
                          "midPlayerName": "Faker",
                          "adcPlayerId": "6972edb355a6e8f16d8e8d7d",
                          "adcPlayerName": "Gumayusi",
                          "supportPlayerId": "6972edbc55a6e8f16d8e8d9c",
                          "supportPlayerName": "Keria"
                        },
                        "championshipMatch": {
                          "isChampionshipRoster": true,
                          "matchedChampionship": "WORLDS",
                          "matchedYear": 2023
                        },
                        "community": {
                          "isPublic": true,
                          "likeCount": 42,
                          "commentCount": 5
                        },
                        "gameMode": "NORMAL",
                        "rank": {
                          "score": 1500,
                          "tier": "GOLD"
                        },
                        "createdAt": "2026-01-20T10:00:00"
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "로스터를 찾을 수 없음", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": false,
                      "code": "ROSTER_NOT_FOUND",
                      "message": "Roster not found",
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<RosterResponse>> getRoster(
            @Parameter(description = "로스터 ID", example = "6789abc123") @PathVariable String id);

    @Operation(summary = "내 로스터 목록 조회", description = "현재 로그인한 사용자의 로스터 목록을 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "items": [
                          {
                            "id": "6789abc123",
                            "userId": "user123",
                            "userName": "Summoner1",
                            "players": {
                              "topPlayerName": "Zeus",
                              "junglePlayerName": "Oner",
                              "midPlayerName": "Faker",
                              "adcPlayerName": "Gumayusi",
                              "supportPlayerName": "Keria"
                            },
                            "championshipMatch": {
                              "isChampionshipRoster": true,
                              "matchedChampionship": "WORLDS",
                              "matchedYear": 2023
                            },
                            "community": {
                              "isPublic": false,
                              "likeCount": 0,
                              "commentCount": 0
                            },
                            "gameMode": "NORMAL",
                            "createdAt": "2026-01-20T10:00:00"
                          }
                        ],
                        "pagination": {
                          "currentPage": 1,
                          "pageSize": 10,
                          "totalItems": 1,
                          "totalPages": 1,
                          "hasNextPage": false,
                          "hasPrevPage": false
                        }
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요")
    })
    ResponseEntity<ApiResponse<PageResponse<RosterResponse>>> getMyRosters(
            @Parameter(hidden = true) User user,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size);

    @Operation(summary = "특정 유저 로스터 목록 조회", description = "특정 사용자의 공개된 로스터 목록을 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<ApiResponse<PageResponse<RosterResponse>>> getUserRosters(
            @Parameter(description = "사용자 ID", example = "user123") @PathVariable String userId,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size);

    @Operation(summary = "로스터 검색", description = "다양한 조건(공개여부, 우승매칭, 게임모드, 티어)으로 로스터를 검색합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "items": [],
                        "pagination": {
                          "currentPage": 1,
                          "pageSize": 10,
                          "totalItems": 0,
                          "totalPages": 0,
                          "hasNextPage": false,
                          "hasPrevPage": false
                        }
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<PageResponse<RosterResponse>>> searchRosters(
            @Parameter(description = "공개 여부 필터", example = "true") @RequestParam(required = false) Boolean isPublic,
            @Parameter(description = "우승 로스터 매칭 여부", example = "true") @RequestParam(required = false) Boolean isChampionship,
            @Parameter(description = "게임 모드 (NORMAL, RANKED)", example = "NORMAL") @RequestParam(required = false) String gameMode,
            @Parameter(description = "티어 필터 (BRONZE, SILVER, GOLD, PLATINUM, DIAMOND)", example = "GOLD") @RequestParam(required = false) String tier,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size);

    @Operation(summary = "좋아요 토글", description = "로스터에 좋아요를 누르거나 취소합니다. 한 번 누르면 좋아요, 다시 누르면 취소됩니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "좋아요 토글 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "id": "6789abc123",
                        "community": {
                          "isPublic": true,
                          "likeCount": 43,
                          "commentCount": 5
                        }
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "로스터를 찾을 수 없음")
    })
    ResponseEntity<ApiResponse<RosterResponse>> toggleLike(
            @Parameter(description = "로스터 ID", example = "6789abc123") @PathVariable String id,
            @Parameter(hidden = true) User user);

    @Operation(summary = "로스터 삭제", description = "내가 생성한 로스터를 삭제합니다. 본인의 로스터만 삭제할 수 있습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "message": "Roster deleted successfully",
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "삭제 권한 없음 (본인의 로스터가 아님)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "로스터를 찾을 수 없음")
    })
    ResponseEntity<ApiResponse<Void>> deleteRoster(
            @Parameter(description = "로스터 ID", example = "6789abc123") @PathVariable String id,
            @Parameter(hidden = true) User user);
}
