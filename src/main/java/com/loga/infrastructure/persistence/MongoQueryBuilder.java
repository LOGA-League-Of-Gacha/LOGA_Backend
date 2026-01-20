package com.loga.infrastructure.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * MongoDB Specification 패턴 구현
 * JPA Specification과 유사한 동적 쿼리 빌더
 */
public class MongoQueryBuilder {

    private final List<Criteria> criteriaList = new ArrayList<>();
    private Pageable pageable;
    private String[] includeFields;
    private String[] excludeFields;

    private MongoQueryBuilder() {}

    public static MongoQueryBuilder builder() {
        return new MongoQueryBuilder();
    }

    /**
     * equals 조건
     */
    public MongoQueryBuilder eq(String field, Object value) {
        if (value != null) {
            criteriaList.add(Criteria.where(field).is(value));
        }
        return this;
    }

    /**
     * not equals 조건
     */
    public MongoQueryBuilder ne(String field, Object value) {
        if (value != null) {
            criteriaList.add(Criteria.where(field).ne(value));
        }
        return this;
    }

    /**
     * in 조건
     */
    public MongoQueryBuilder in(String field, Collection<?> values) {
        if (values != null && !values.isEmpty()) {
            criteriaList.add(Criteria.where(field).in(values));
        }
        return this;
    }

    /**
     * like 조건 (regex)
     */
    public MongoQueryBuilder like(String field, String value) {
        if (value != null && !value.isEmpty()) {
            criteriaList.add(Criteria.where(field).regex(value, "i"));
        }
        return this;
    }

    /**
     * contains 조건 (부분 문자열 검색)
     */
    public MongoQueryBuilder contains(String field, String value) {
        if (value != null && !value.isEmpty()) {
            criteriaList.add(Criteria.where(field).regex(".*" + value + ".*", "i"));
        }
        return this;
    }

    /**
     * greater than
     */
    public MongoQueryBuilder gt(String field, Object value) {
        if (value != null) {
            criteriaList.add(Criteria.where(field).gt(value));
        }
        return this;
    }

    /**
     * greater than or equals
     */
    public MongoQueryBuilder gte(String field, Object value) {
        if (value != null) {
            criteriaList.add(Criteria.where(field).gte(value));
        }
        return this;
    }

    /**
     * less than
     */
    public MongoQueryBuilder lt(String field, Object value) {
        if (value != null) {
            criteriaList.add(Criteria.where(field).lt(value));
        }
        return this;
    }

    /**
     * less than or equals
     */
    public MongoQueryBuilder lte(String field, Object value) {
        if (value != null) {
            criteriaList.add(Criteria.where(field).lte(value));
        }
        return this;
    }

    /**
     * between
     */
    public MongoQueryBuilder between(String field, Object from, Object to) {
        if (from != null && to != null) {
            criteriaList.add(Criteria.where(field).gte(from).lte(to));
        }
        return this;
    }

    /**
     * exists
     */
    public MongoQueryBuilder exists(String field, boolean exists) {
        criteriaList.add(Criteria.where(field).exists(exists));
        return this;
    }

    /**
     * null check
     */
    public MongoQueryBuilder isNull(String field) {
        criteriaList.add(Criteria.where(field).is(null));
        return this;
    }

    /**
     * not null check
     */
    public MongoQueryBuilder isNotNull(String field) {
        criteriaList.add(Criteria.where(field).ne(null));
        return this;
    }

    /**
     * 조건부 추가
     */
    public MongoQueryBuilder addIf(boolean condition, Consumer<MongoQueryBuilder> builderConsumer) {
        if (condition) {
            builderConsumer.accept(this);
        }
        return this;
    }

    /**
     * OR 조건 그룹
     */
    public MongoQueryBuilder or(Consumer<OrBuilder> orBuilderConsumer) {
        OrBuilder orBuilder = new OrBuilder();
        orBuilderConsumer.accept(orBuilder);
        if (!orBuilder.getCriteriaList().isEmpty()) {
            criteriaList.add(new Criteria().orOperator(orBuilder.getCriteriaList().toArray(new Criteria[0])));
        }
        return this;
    }

    /**
     * 페이지네이션 설정
     */
    public MongoQueryBuilder pageable(Pageable pageable) {
        this.pageable = pageable;
        return this;
    }

    /**
     * 필드 포함
     */
    public MongoQueryBuilder include(String... fields) {
        this.includeFields = fields;
        return this;
    }

    /**
     * 필드 제외
     */
    public MongoQueryBuilder exclude(String... fields) {
        this.excludeFields = fields;
        return this;
    }

    /**
     * Query 객체 빌드
     */
    public Query build() {
        Query query = new Query();

        if (!criteriaList.isEmpty()) {
            Criteria finalCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
            query.addCriteria(finalCriteria);
        }

        if (pageable != null) {
            query.with(pageable);
        }

        if (includeFields != null) {
            for (String field : includeFields) {
                query.fields().include(field);
            }
        }

        if (excludeFields != null) {
            for (String field : excludeFields) {
                query.fields().exclude(field);
            }
        }

        return query;
    }

    /**
     * OR 조건 빌더
     */
    public static class OrBuilder {
        private final List<Criteria> criteriaList = new ArrayList<>();

        public OrBuilder eq(String field, Object value) {
            if (value != null) {
                criteriaList.add(Criteria.where(field).is(value));
            }
            return this;
        }

        public OrBuilder like(String field, String value) {
            if (value != null && !value.isEmpty()) {
                criteriaList.add(Criteria.where(field).regex(value, "i"));
            }
            return this;
        }

        public OrBuilder contains(String field, String value) {
            if (value != null && !value.isEmpty()) {
                criteriaList.add(Criteria.where(field).regex(".*" + value + ".*", "i"));
            }
            return this;
        }

        List<Criteria> getCriteriaList() {
            return criteriaList;
        }
    }
}
