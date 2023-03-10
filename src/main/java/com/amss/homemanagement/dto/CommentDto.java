package com.amss.homemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentDto(
        @Null
        UUID id,

        @NotBlank
        String content,

        @Null
        LocalDateTime creationDate,

        @Null
        UUID userId,

        UUID taskId
) { }
