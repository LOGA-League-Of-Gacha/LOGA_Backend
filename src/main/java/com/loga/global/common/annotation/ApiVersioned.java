package com.loga.global.common.annotation;

import java.lang.annotation.*;

/**
 * API 버전을 명시하는 어노테이션
 *
 * 컨트롤러 메서드에 사용하여 특정 버전에서만 동작하게 합니다.
 *
 * <pre>
 * {@literal @}ApiVersioned("2")
 * {@literal @}GetMapping("/new-feature")
 * public Response newFeature() {
 *     // v2에서만 사용 가능
 * }
 *
 * {@literal @}ApiVersioned({"1", "2"})
 * {@literal @}GetMapping("/common")
 * public Response common() {
 *     // v1, v2 모두 사용 가능
 * }
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersioned {

    /**
     * 지원하는 API 버전 목록
     * 예: "1", "2" 또는 {"1", "2"}
     */
    String[] value();
}
