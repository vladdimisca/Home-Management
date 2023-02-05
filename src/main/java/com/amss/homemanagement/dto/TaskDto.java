package com.amss.homemanagement.dto;

import com.amss.homemanagement.model.Priority;
import com.amss.homemanagement.model.State;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;
import java.util.UUID;
public record TaskDto(

        @Null
        UUID id,

        @NotBlank
        String title,

        @NotBlank
        String description,

        State state,

        @NotNull
        Priority priority,

        @Null
        LocalDateTime creationDate,

        @NotNull
        UUID familyId,


        UUID creatorId,

        UUID assigneeId

) { }
