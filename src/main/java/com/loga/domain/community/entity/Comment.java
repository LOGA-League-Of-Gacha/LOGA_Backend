package com.loga.domain.community.entity;

import com.loga.infrastructure.persistence.BaseDocument;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 댓글 도메인 엔티티
 */
@Document(collection = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Comment extends BaseDocument {

    @Indexed
    private String rosterId;

    private String userId;

    private String userName;

    private String userProfileImage;

    private String content;

    // ===== Factory Methods =====

    public static Comment create(String rosterId, String userId, String userName,
                                  String userProfileImage, String content) {
        return Comment.builder()
                .rosterId(rosterId)
                .userId(userId)
                .userName(userName)
                .userProfileImage(userProfileImage)
                .content(content)
                .build();
    }

    // ===== Domain Logic =====

    public boolean isOwnedBy(String userId) {
        return this.userId.equals(userId);
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
