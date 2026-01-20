package com.loga.global.common.dto.response;

import lombok.Builder;
import lombok.Getter;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * 페이지네이션 응답 DTO NestJS 형식과 동일한 구조: { items: [], pagination: {} }
 */
@Getter
@Builder
public class PageResponse<T> {

    private List<T> items;
    private Pagination pagination;

    @Getter
    @Builder
    public static class Pagination {

        private int currentPage;
        private int pageSize;
        private long totalItems;
        private int totalPages;
        private boolean hasNextPage;
        private boolean hasPrevPage;
    }

    /**
     * Spring Data Page를 PageResponse로 변환
     */
    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
                           .items(page.getContent())
                           .pagination(Pagination.builder()
                                                 .currentPage(page.getNumber() + 1)  // 1부터 시작
                                                 .pageSize(page.getSize())
                                                 .totalItems(page.getTotalElements())
                                                 .totalPages(page.getTotalPages())
                                                 .hasNextPage(page.hasNext())
                                                 .hasPrevPage(page.hasPrevious())
                                                 .build())
                           .build();
    }

    /**
     * 엔티티 -> DTO 변환과 함께 PageResponse 생성
     */
    public static <T, R> PageResponse<R> from(Page<T> page, Function<T, R> converter) {
        List<R> items = page.getContent()
                            .stream()
                            .map(converter)
                            .toList();

        return PageResponse.<R>builder()
                           .items(items)
                           .pagination(Pagination.builder()
                                                 .currentPage(page.getNumber() + 1)  // 1부터 시작
                                                 .pageSize(page.getSize())
                                                 .totalItems(page.getTotalElements())
                                                 .totalPages(page.getTotalPages())
                                                 .hasNextPage(page.hasNext())
                                                 .hasPrevPage(page.hasPrevious())
                                                 .build())
                           .build();
    }
}
