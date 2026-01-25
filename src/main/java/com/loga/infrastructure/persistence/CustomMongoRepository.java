package com.loga.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

/**
 * MongoDB 커스텀 리포지토리 인터페이스 Specification 패턴 지원
 */
public interface CustomMongoRepository<T> {

    /**
     * 동적 쿼리로 단일 조회
     */
    Optional<T> findOne(Query query);

    /**
     * 동적 쿼리로 목록 조회
     */
    List<T> findAll(Query query);

    /**
     * 동적 쿼리로 페이징 조회
     */
    Page<T> findAll(Query query, Pageable pageable);

    /**
     * 동적 쿼리로 카운트
     */
    long count(Query query);

    /**
     * 동적 쿼리로 존재 여부 확인
     */
    boolean exists(Query query);

    /**
     * MongoQueryBuilder로 목록 조회
     */
    default List<T> findAll(MongoQueryBuilder builder) {
        return findAll(builder.build());
    }

    /**
     * MongoQueryBuilder로 페이징 조회
     */
    default Page<T> findAll(MongoQueryBuilder builder, Pageable pageable) {
        return findAll(builder.build(), pageable);
    }
}
