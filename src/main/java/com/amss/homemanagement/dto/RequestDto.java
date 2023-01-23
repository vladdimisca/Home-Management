package com.amss.homemanagement.dto;

import com.amss.homemanagement.model.RequestStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.UUID;

public record RequestDto(
        @Null
        UUID id,

        @Null
        RequestStatus status,

        @NotNull
        UUID familyId
) { }
