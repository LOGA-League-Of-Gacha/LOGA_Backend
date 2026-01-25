package com.loga.domain.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequest {
    @NotBlank(message = "Content is required")
    @Size(max = 500, message = "Comment must be less than 500 characters")
    private String content;
}
