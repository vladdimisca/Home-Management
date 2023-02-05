package com.amss.homemanagement.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationDto(
        @Null
        UUID id,

        @Null
        LocalDateTime date,

        @Null
        UUID userId,

        @NotNull
        UUID taskId
) { }
