package com.amss.homemanagement.dto;

import com.amss.homemanagement.model.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.UUID;

public record FamilyMemberDto(
        @NotNull
        Role role,

        @Null
        Boolean isAdmin,

        @Null
        UUID familyId,

        @Null
        UUID memberId
) {}
