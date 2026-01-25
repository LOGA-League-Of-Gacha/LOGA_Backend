package com.loga.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * API 버전닝 설정
 *
 * 헤더 기반 버전 Fallback 패턴 지원: - X-API-Version: 1 또는 2 - Accept-Version: v1 또는 v2
 *
 * 현재 버전: v1 (기본값)
 *
 * 버전이 명시되지 않거나 존재하지 않는 버전은 자동으로 v1으로 fallback
 */
@Configuration
public class ApiVersioningConfig implements WebMvcConfigurer {

    public static final String CURRENT_VERSION = "1";
    public static final String VERSION_HEADER = "X-API-Version";
    public static final String ACCEPT_VERSION_HEADER = "Accept-Version";
}
