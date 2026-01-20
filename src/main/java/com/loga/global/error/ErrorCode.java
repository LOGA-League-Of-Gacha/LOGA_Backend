package com.loga.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

/**
 * 애플리케이션 에러 코드 정의
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common (C)
    INVALID_INPUT("C001", HttpStatus.BAD_REQUEST, "Invalid input"),
    INTERNAL_ERROR("C002", HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    RESOURCE_NOT_FOUND("C003", HttpStatus.NOT_FOUND, "Resource not found"),

    // Auth (A)
    UNAUTHORIZED("A001", HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN("A002", HttpStatus.FORBIDDEN, "Access denied"),
    INVALID_TOKEN("A003", HttpStatus.UNAUTHORIZED, "Invalid or expired token"),
    TOKEN_EXPIRED("A004", HttpStatus.UNAUTHORIZED, "Token has expired"),

    // User (U)
    USER_NOT_FOUND("U001", HttpStatus.NOT_FOUND, "User not found"),
    USER_ALREADY_EXISTS("U002", HttpStatus.CONFLICT, "User already exists"),
    INVALID_USER_STATUS("U003", HttpStatus.BAD_REQUEST, "Invalid user status"),

    // Player (P)
    PLAYER_NOT_FOUND("P001", HttpStatus.NOT_FOUND, "Player not found"),
    NO_PLAYERS_AVAILABLE("P002", HttpStatus.NOT_FOUND, "No players available for this position"),

    // Roster (R)
    ROSTER_NOT_FOUND("R001", HttpStatus.NOT_FOUND, "Roster not found"),
    INVALID_ROSTER("R002", HttpStatus.BAD_REQUEST, "Invalid roster configuration"),
    ROSTER_ALREADY_EXISTS("R003", HttpStatus.CONFLICT, "Roster already exists"),

    // Community (CM)
    COMMENT_NOT_FOUND("CM001", HttpStatus.NOT_FOUND, "Comment not found"),

    // Report (RP)
    REPORT_NOT_FOUND("RP001", HttpStatus.NOT_FOUND, "Bug report not found"),

    // Gacha (G)
    NO_REROLL_LEFT("G001", HttpStatus.BAD_REQUEST,
                   "No reroll attempts left. Upgrade to premium for unlimited rerolls."),
    GACHA_LIMIT_EXCEEDED("G002", HttpStatus.TOO_MANY_REQUESTS, "Daily gacha limit exceeded");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
