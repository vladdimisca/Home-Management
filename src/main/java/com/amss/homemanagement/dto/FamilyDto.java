package com.amss.homemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

import java.util.UUID;

public record FamilyDto(
        @Null
        UUID id,

        @NotBlank
        String name
) { }
