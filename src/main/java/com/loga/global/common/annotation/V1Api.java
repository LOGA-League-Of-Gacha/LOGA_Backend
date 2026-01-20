package com.loga.global.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * API v1 컨트롤러를 위한 편의 어노테이션
 *
 * @RestController + @RequestMapping("/api/v1/{path}") 조합
 *
 * <pre>
 * {@literal @}V1Api("players")
 * public class PlayerController { }
 * // -> /api/v1/players
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping
public @interface V1Api {

    /**
     * API 경로 (예: "players", "rosters")
     */
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] value() default {};
}
