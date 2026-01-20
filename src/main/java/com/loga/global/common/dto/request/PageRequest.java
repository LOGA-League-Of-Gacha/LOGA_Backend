package com.loga.global.common.dto.request;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 페이지네이션 요청 DTO NestJS 형식과 동일: page (1부터 시작), limit (기본값: 10, 최대: 100)
 */
@Getter
@Setter
public class PageRequest {

    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LIMIT = 100;

    /**
     * 페이지 번호 (1부터 시작, 기본값: 1)
     */
    private int page = 1;

    /**
     * 한 페이지에 표시할 데이터 수 (기본값: 10, 최대: 100)
     */
    private int limit = DEFAULT_LIMIT;

    /**
     * Spring Data Pageable로 변환 page는 내부적으로 0-based로 변환됨
     */
    public Pageable toPageable() {
        int safePage = Math.max(0, page - 1);  // 1-based -> 0-based
        int safeLimit = Math.min(Math.max(1, limit), MAX_LIMIT);
        return org.springframework.data.domain.PageRequest.of(safePage, safeLimit);
    }

    /**
     * 정렬 포함 Pageable로 변환
     */
    public Pageable toPageable(Sort sort) {
        int safePage = Math.max(0, page - 1);
        int safeLimit = Math.min(Math.max(1, limit), MAX_LIMIT);
        return org.springframework.data.domain.PageRequest.of(safePage, safeLimit, sort);
    }

    /**
     * 정렬 포함 Pageable로 변환 (단일 필드)
     */
    public Pageable toPageable(String sortBy, boolean desc) {
        Sort sort = Sort.by(desc ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return toPageable(sort);
    }

    // === 빌더 스타일 팩토리 메서드 ===

    public static PageRequest of(int page, int limit) {
        PageRequest request = new PageRequest();
        request.setPage(page);
        request.setLimit(limit);
        return request;
    }

    /**
     * 빌더 패턴 지원
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int page = 1;
        private int limit = DEFAULT_LIMIT;
        private Sort sort = Sort.unsorted();

        public Builder page(int page) {
            this.page = Math.max(1, page);
            return this;
        }

        public Builder limit(int limit) {
            this.limit = Math.min(Math.max(1, limit), MAX_LIMIT);
            return this;
        }

        public Builder sortBy(String property) {
            this.sort = Sort.by(Sort.Direction.ASC, property);
            return this;
        }

        public Builder sortByDesc(String property) {
            this.sort = Sort.by(Sort.Direction.DESC, property);
            return this;
        }

        public Builder sort(Sort sort) {
            this.sort = sort;
            return this;
        }

        public Pageable toPageable() {
            int safePage = Math.max(0, page - 1);
            int safeLimit = Math.min(Math.max(1, limit), MAX_LIMIT);
            return org.springframework.data.domain.PageRequest.of(safePage, safeLimit, sort);
        }

        public PageRequest build() {
            PageRequest request = new PageRequest();
            request.setPage(page);
            request.setLimit(limit);
            return request;
        }
    }
}
