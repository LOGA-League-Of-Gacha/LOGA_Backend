package com.loga.domain.community.repository;

import com.loga.domain.community.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 댓글 레포지토리
 */
@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    Page<Comment> findByRosterIdOrderByCreatedAtDesc(String rosterId, Pageable pageable);

    List<Comment> findByRosterId(String rosterId);

    long countByRosterId(String rosterId);

    void deleteByRosterId(String rosterId);
}
