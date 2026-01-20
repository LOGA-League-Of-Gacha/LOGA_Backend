package com.loga.domain.community.dto;

import com.loga.domain.community.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {
    private String id;
    private String rosterId;
    private String userId;
    private String userName;
    private String userProfileImage;
    private String content;
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .rosterId(comment.getRosterId())
                .userId(comment.getUserId())
                .userName(comment.getUserName())
                .userProfileImage(comment.getUserProfileImage())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
