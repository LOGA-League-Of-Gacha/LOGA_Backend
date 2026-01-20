package com.loga.domain.community.service;

import com.loga.domain.community.dto.CommentResponse;
import com.loga.domain.community.dto.CreateCommentRequest;
import com.loga.domain.community.entity.Comment;
import com.loga.domain.community.repository.CommentRepository;
import com.loga.domain.roster.dto.RosterResponse;
import com.loga.domain.roster.entity.Roster;
import com.loga.domain.roster.repository.RosterRepository;
import com.loga.domain.user.entity.User;
import com.loga.global.common.dto.response.PageResponse;
import com.loga.global.error.BusinessException;
import com.loga.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {

    private final RosterRepository rosterRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public PageResponse<RosterResponse> getCommunityRosters(Pageable pageable) {
        Page<Roster> page = rosterRepository.findPublicRosters(pageable);
        return PageResponse.from(page, RosterResponse::from);
    }

    @Transactional(readOnly = true)
    public PageResponse<RosterResponse> getPopularRosters(Pageable pageable) {
        Page<Roster> page = rosterRepository.findByPopularity(pageable);
        return PageResponse.from(page, RosterResponse::from);
    }

    @Transactional(readOnly = true)
    public PageResponse<RosterResponse> getChampionshipRosters(Pageable pageable) {
        Page<Roster> page = rosterRepository.findPublicChampionshipRosters(pageable);
        return PageResponse.from(page, RosterResponse::from);
    }

    @Transactional(readOnly = true)
    public PageResponse<CommentResponse> getComments(String rosterId, Pageable pageable) {
        Page<Comment> page = commentRepository.findByRosterIdOrderByCreatedAtDesc(rosterId, pageable);
        return PageResponse.from(page, CommentResponse::from);
    }

    @Transactional
    public CommentResponse createComment(String rosterId, CreateCommentRequest request, User user) {
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROSTER_NOT_FOUND));

        Comment comment = Comment.create(
                rosterId,
                user.getId(),
                user.getNickname(),
                user.getProfileImage(),
                request.getContent()
        );
        comment = commentRepository.save(comment);

        roster.addComment();
        rosterRepository.save(roster);

        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(String rosterId, String commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.isOwnedBy(user.getId()) && user.getRole() != User.Role.ADMIN) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        commentRepository.delete(comment);

        rosterRepository.findById(rosterId).ifPresent(roster -> {
            roster.removeComment();
            rosterRepository.save(roster);
        });
    }
}
