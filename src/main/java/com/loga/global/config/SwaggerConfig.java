package com.loga.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT-Auth");

        return new OpenAPI()
                .info(apiInfo())
                .servers(getServers())
                .components(new Components().addSecuritySchemes("JWT-Auth", securityScheme))
                .addSecurityItem(securityRequirement);
    }

    private Info apiInfo() {
        return new Info()
                .title("LOGA - League of Gacha API")
                .description("""
                    ## LOL 프로게이머 가챠 게임 백엔드 API

                    ### 주요 기능
                    - **가챠 시스템**: 포지션별/전체 로스터 랜덤 뽑기
                    - **선수 데이터**: 200+ 프로 선수 정보 조회
                    - **로스터 관리**: 나만의 드림팀 저장 및 공유
                    - **커뮤니티**: 좋아요, 댓글, 공개 로스터 갤러리
                    - **우승 로스터 매칭**: 실제 대회 우승팀 완성 시 특별 효과

                    ### 인증
                    - Google OAuth2 로그인 지원
                    - JWT Bearer 토큰 인증

                    ### API 버전 (헤더 기반)
                    - **현재 버전**: 1.0.0
                    - **버전 헤더**: `X-API-Version: 1` 또는 `Accept-Version: v1`
                    - **Fallback**: 헤더 없거나 미지원 버전 → v1 자동 적용
                    - **응답 헤더**: `X-API-Version`으로 실제 적용 버전 반환
                    """)
                .version("1.0.0")
                .contact(new Contact()
                        .name("LOGA Team")
                        .url("https://github.com/LOGA-League-Of-Gacha"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

    private List<Server> getServers() {
        if ("prod".equals(activeProfile)) {
            return List.of(
                    new Server()
                            .url("https://api.league-of-gacha.com")
                            .description("Production Server")
            );
        }

        return List.of(
                new Server()
                        .url("http://localhost:8080")
                        .description("Local Development Server"),
                new Server()
                        .url("https://api.league-of-gacha.com")
                        .description("Production Server")
        );
    }
}
