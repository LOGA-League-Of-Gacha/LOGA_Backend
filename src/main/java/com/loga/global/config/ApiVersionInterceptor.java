package com.loga.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * API 버전 Interceptor
 *
 * 요청 헤더에서 API 버전을 추출하고 처리합니다.
 *
 * 지원하는 헤더: - X-API-Version: 1, 2 (숫자) - Accept-Version: v1, v2 (v prefix)
 *
 * 동작: 1. 헤더에서 버전 추출 2. 지원하지 않는 버전이면 기본 버전(v1)으로 fallback 3. 응답 헤더에 실제 사용된 버전 명시
 */
@Slf4j
@Component
public class ApiVersionInterceptor implements HandlerInterceptor {

    private static final String DEFAULT_VERSION = "1";
    private static final String[] SUPPORTED_VERSIONS = {"1", "2"};

    public static final String API_VERSION_ATTRIBUTE = "apiVersion";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String version = extractVersion(request);
        String resolvedVersion = resolveVersion(version);

        // 요청 속성에 버전 저장 (컨트롤러에서 사용 가능)
        request.setAttribute(API_VERSION_ATTRIBUTE, resolvedVersion);

        // 응답 헤더에 실제 사용된 버전 명시
        response.setHeader("X-API-Version", resolvedVersion);

        // Fallback이 발생한 경우 로그
        if (version != null && !version.equals(resolvedVersion)) {
            log.debug("API version fallback: requested={}, resolved={}", version, resolvedVersion);
        }

        return true;
    }

    /**
     * 요청 헤더에서 버전 추출
     */
    private String extractVersion(HttpServletRequest request) {
        // 1. X-API-Version 헤더 확인
        String version = request.getHeader(ApiVersioningConfig.VERSION_HEADER);
        if (version != null && !version.isBlank()) {
            return version.trim();
        }

        // 2. Accept-Version 헤더 확인
        version = request.getHeader(ApiVersioningConfig.ACCEPT_VERSION_HEADER);
        if (version != null && !version.isBlank()) {
            // "v1" -> "1" 변환
            return version.trim().toLowerCase().replace("v", "");
        }

        return null;
    }

    /**
     * 버전 해석 및 Fallback 처리
     */
    private String resolveVersion(String requestedVersion) {
        if (requestedVersion == null) {
            return DEFAULT_VERSION;
        }

        // 지원하는 버전인지 확인
        for (String supported : SUPPORTED_VERSIONS) {
            if (supported.equals(requestedVersion)) {
                return requestedVersion;
            }
        }

        // 지원하지 않는 버전 -> 기본 버전으로 fallback
        return DEFAULT_VERSION;
    }
}
