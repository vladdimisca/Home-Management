package com.amss.homemanagement.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;
import java.util.UUID;

public record LikeDto(
        @Null
        UUID id,

        @Null
        LocalDateTime creationDate,

        @Null
        UUID userId,

        @NotNull
        UUID commentId
) { }
