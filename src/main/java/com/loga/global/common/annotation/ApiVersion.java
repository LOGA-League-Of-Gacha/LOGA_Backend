package com.loga.global.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

/**
 * API 버전을 지정하는 어노테이션
 *
 * 클래스 또는 메서드 레벨에서 사용 가능하며,
 * 지정된 버전에 대해 /api/v{version} prefix가 자동으로 적용됩니다.
 *
 * <pre>
 * 사용 예시:
 *
 * // 단일 버전
 * {@literal @}ApiVersion("1")
 * {@literal @}RestController
 * public class PlayerController { }
 * // -> /api/v1/players
 *
 * // 다중 버전 (fallback)
 * {@literal @}ApiVersion({"1", "2"})
 * {@literal @}RestController
 * public class PlayerController { }
 * // -> /api/v1/players, /api/v2/players 모두 동일 컨트롤러로 처리
 *
 * // 특정 버전만
 * {@literal @}ApiVersion("2")
 * {@literal @}GetMapping("/new-feature")
 * public Response newFeature() { }
 * // -> /api/v2/players/new-feature (v2에서만 사용 가능)
 * </pre>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping
public @interface ApiVersion {

    /**
     * API 버전 (예: "1", "2")
     * 여러 버전을 배열로 지정하면 모든 버전에서 접근 가능 (fallback)
     */
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] value() default {};
}
