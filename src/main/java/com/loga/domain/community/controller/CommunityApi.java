package com.loga.domain.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.loga.domain.community.dto.CommentResponse;
import com.loga.domain.community.dto.CreateCommentRequest;
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
 * 커뮤니티 API 인터페이스 - Swagger 문서화
 */
@Tag(name = "Community", description = "커뮤니티 - 로스터 공유, 인기 로스터, 댓글 API")
public interface CommunityApi {

    @Operation(summary = "공개 로스터 목록 조회", description = "커뮤니티에 공개된 모든 로스터를 최신순으로 조회합니다.")
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
                            "userName": "T1Fan",
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
                              "isPublic": true,
                              "likeCount": 156,
                              "commentCount": 23
                            },
                            "gameMode": "NORMAL",
                            "createdAt": "2026-01-24T18:30:00"
                          }
                        ],
                        "pagination": {
                          "currentPage": 1,
                          "pageSize": 10,
                          "totalItems": 100,
                          "totalPages": 10,
                          "hasNextPage": true,
                          "hasPrevPage": false
                        }
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<PageResponse<RosterResponse>>> getCommunityRosters(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size);

    @Operation(summary = "인기 로스터 조회", description = "좋아요가 많은 인기 로스터를 조회합니다. 좋아요 수 기준 내림차순 정렬됩니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "items": [
                          {
                            "id": "abc123",
                            "userName": "LOLPro",
                            "players": {
                              "topPlayerName": "TheShy",
                              "junglePlayerName": "Canyon",
                              "midPlayerName": "Faker",
                              "adcPlayerName": "Ruler",
                              "supportPlayerName": "Keria"
                            },
                            "community": {
                              "isPublic": true,
                              "likeCount": 892,
                              "commentCount": 156
                            },
                            "gameMode": "NORMAL",
                            "createdAt": "2026-01-15T12:00:00"
                          }
                        ],
                        "pagination": {
                          "currentPage": 1,
                          "pageSize": 10,
                          "totalItems": 50,
                          "totalPages": 5,
                          "hasNextPage": true,
                          "hasPrevPage": false
                        }
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<PageResponse<RosterResponse>>> getPopularRosters(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size);

    @Operation(summary = "우승 로스터 매칭 목록 조회", description = "실제 대회 우승 로스터와 매칭된 로스터들을 조회합니다. (월즈, MSI 등 우승 라인업과 동일한 선수 조합)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "items": [
                          {
                            "id": "champ123",
                            "userName": "LegendFan",
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
                              "isPublic": true,
                              "likeCount": 1024,
                              "commentCount": 89
                            }
                          }
                        ],
                        "pagination": {
                          "currentPage": 1,
                          "pageSize": 10,
                          "totalItems": 25,
                          "totalPages": 3,
                          "hasNextPage": true,
                          "hasPrevPage": false
                        }
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<PageResponse<RosterResponse>>> getChampionshipRosters(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size);

    @Operation(summary = "로스터 댓글 조회", description = "특정 로스터의 댓글 목록을 페이징하여 조회합니다. 최신순으로 정렬됩니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "items": [
                          {
                            "id": "comment123",
                            "rosterId": "6789abc123",
                            "userId": "user456",
                            "userName": "LOLFan",
                            "userProfileImage": "https://example.com/profile.jpg",
                            "content": "역대급 드림팀이네요!",
                            "createdAt": "2026-01-25T10:30:00"
                          },
                          {
                            "id": "comment124",
                            "rosterId": "6789abc123",
                            "userId": "user789",
                            "userName": "ProGamer",
                            "userProfileImage": null,
                            "content": "페이커 선수 최고!",
                            "createdAt": "2026-01-25T09:15:00"
                          }
                        ],
                        "pagination": {
                          "currentPage": 1,
                          "pageSize": 20,
                          "totalItems": 45,
                          "totalPages": 3,
                          "hasNextPage": true,
                          "hasPrevPage": false
                        }
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "로스터를 찾을 수 없음")
    })
    ResponseEntity<ApiResponse<PageResponse<CommentResponse>>> getComments(
            @Parameter(description = "로스터 ID", example = "6789abc123") @PathVariable String rosterId,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "20") @RequestParam(defaultValue = "20") int size);

    @Operation(summary = "댓글 작성", description = "로스터에 댓글을 작성합니다. 로그인이 필요합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "댓글 작성 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "id": "comment125",
                        "rosterId": "6789abc123",
                        "userId": "user123",
                        "userName": "MyNickname",
                        "userProfileImage": "https://example.com/myprofile.jpg",
                        "content": "정말 멋진 로스터네요!",
                        "createdAt": "2026-01-25T14:35:00"
                      },
                      "timestamp": "2026-01-25T14:35:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (내용 누락 또는 500자 초과)", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": false,
                      "code": "VALIDATION_ERROR",
                      "message": "Comment must be less than 500 characters",
                      "timestamp": "2026-01-25T14:35:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "로스터를 찾을 수 없음")
    })
    ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @Parameter(description = "로스터 ID", example = "6789abc123") @PathVariable String rosterId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "댓글 작성 요청", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateCommentRequest.class), examples = @ExampleObject(value = """
                    {
                      "content": "정말 멋진 로스터네요!"
                    }
                    """))) @RequestBody CreateCommentRequest request,
            @Parameter(hidden = true) User user);

    @Operation(summary = "댓글 삭제", description = "내가 작성한 댓글을 삭제합니다. 본인의 댓글만 삭제할 수 있습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "message": "Comment deleted",
                      "timestamp": "2026-01-25T14:40:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "삭제 권한 없음 (본인의 댓글이 아님)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "댓글 또는 로스터를 찾을 수 없음")
    })
    ResponseEntity<ApiResponse<Void>> deleteComment(
            @Parameter(description = "로스터 ID", example = "6789abc123") @PathVariable String rosterId,
            @Parameter(description = "댓글 ID", example = "comment123") @PathVariable String commentId,
            @Parameter(hidden = true) User user);
}
