package com.loga.global.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.loga.infrastructure.security.CustomOAuth2UserService;
import com.loga.infrastructure.security.JwtAuthenticationFilter;
import com.loga.infrastructure.security.OAuth2AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

/**
 * Spring Security 설정
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // OAuth2 관련 빈들은 선택적으로 주입 (Google 자격 증명이 없을 수 있음)
    @Autowired(required = false)
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired(required = false)
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Value("#{'${cors.allowed-origins}'.split(',')}")
    private List<String> allowedOrigins;

    @Value("${spring.security.oauth2.client.registration.google.client-id:}")
    private String googleClientId;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // REST API용 AuthenticationEntryPoint (리다이렉트 대신 401 JSON 반환)
        AuthenticationEntryPoint restAuthenticationEntryPoint = (request, response, authException) -> {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"success\":false,\"message\":\"Unauthorized\"}");
        };

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**")
                        .permitAll()
                        .requestMatchers("/oauth2/**", "/login/oauth2/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/players/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/gacha/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/championships/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/rosters/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/community/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/statistics/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/reports")
                        .permitAll()
                        // Swagger UI
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
                                "/swagger-resources/**")
                        .permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info")
                        .permitAll()
                        // Admin endpoints
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")
                        // Authenticated endpoints
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // OAuth2 설정은 Google 자격 증명이 있을 때만 활성화
        // REST API 백엔드: 자동 리다이렉트 완전 비활성화
        if (isOAuth2Enabled()) {
            http.oauth2Login(oauth2 -> oauth2
                    // 가짜 로그인 페이지 설정으로 자동 리다이렉트 비활성화
                    // 프론트엔드에서 직접 /oauth2/authorization/google 호출해야 함
                    .loginPage("/oauth2/authorization/google")
                    .loginProcessingUrl("/login/oauth2/code/*")
                    .authorizationEndpoint(auth -> auth
                            .baseUri("/oauth2/authorization"))
                    .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler((request, response, exception) -> {
                        response.setStatus(401);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("{\"success\":false,\"message\":\"OAuth2 authentication failed: "
                                + exception.getMessage() + "\"}");
                    }));
        }

        // OAuth2 이후에 다시 설정하여 리다이렉트 방지 (최종 오버라이드)
        http.exceptionHandling(ex -> ex.authenticationEntryPoint(restAuthenticationEntryPoint));

        return http.build();
    }

    private boolean isOAuth2Enabled() {
        // OAuth2 자동 리다이렉트 비활성화 (REST API 백엔드)
        // OAuth2 로그인은 프론트엔드에서 직접 /oauth2/authorization/google 호출
        // TODO: OAuth2 로그인 기능 구현 시 별도 설정 필요
        return false;
        /*
         * return googleClientId != null && !googleClientId.isBlank() && !googleClientId.equals("your-google-client-id")
         * && customOAuth2UserService != null && oAuth2AuthenticationSuccessHandler != null;
         */
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(
                Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "X-API-Version", "Accept-Version"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "X-API-Version"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
