package com.loga.global.config;

import com.loga.infrastructure.security.CustomOAuth2UserService;
import com.loga.infrastructure.security.JwtAuthenticationFilter;
import com.loga.infrastructure.security.OAuth2AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

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
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/v1/auth/**")
                        .permitAll()
                        .requestMatchers("/oauth2/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/players/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/gacha/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/championships/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/rosters/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/community/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/statistics/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/reports")
                        .permitAll()
                        // Swagger UI
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-resources/**")
                        .permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info")
                        .permitAll()
                        // Admin endpoints
                        .requestMatchers("/api/v1/admin/**")
                        .hasRole("ADMIN")
                        // Authenticated endpoints
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // OAuth2 설정은 Google 자격 증명이 있을 때만 활성화
        if (isOAuth2Enabled()) {
            http.oauth2Login(oauth2 -> oauth2
                    .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                    .successHandler(oAuth2AuthenticationSuccessHandler)
            );
        }

        return http.build();
    }

    private boolean isOAuth2Enabled() {
        return googleClientId != null
                && !googleClientId.isBlank()
                && !googleClientId.equals("your-google-client-id")
                && customOAuth2UserService != null
                && oAuth2AuthenticationSuccessHandler != null;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
