package com.loga.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import lombok.RequiredArgsConstructor;

/**
 * MongoDB 커스텀 리포지토리 구현체
 */
@RequiredArgsConstructor
public abstract class CustomMongoRepositoryImpl<T> implements CustomMongoRepository<T> {

    protected final MongoTemplate mongoTemplate;
    private final Class<T> domainClass;

    @Override
    public Optional<T> findOne(Query query) {
        return Optional.ofNullable(mongoTemplate.findOne(query, domainClass));
    }

    @Override
    public List<T> findAll(Query query) {
        return mongoTemplate.find(query, domainClass);
    }

    @Override
    public Page<T> findAll(Query query, Pageable pageable) {
        long total = mongoTemplate.count(Query.of(query)
                .limit(-1)
                .skip(-1), domainClass);
        query.with(pageable);
        List<T> content = mongoTemplate.find(query, domainClass);
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public long count(Query query) {
        return mongoTemplate.count(query, domainClass);
    }

    @Override
    public boolean exists(Query query) {
        return mongoTemplate.exists(query, domainClass);
    }
}
