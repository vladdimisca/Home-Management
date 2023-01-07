package com.amss.homemanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(
        @Null
        UUID id,

        @Email
        @NotNull
        String email,

        @NotNull
        @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
                 message = "must be a valid phone number!")
        String phoneNumber,

        @NotBlank
        String fullName,

        @NotNull
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z]).{6,}",
                message = "must contain at least 6 characters including one digit and one lower case letter")
        String password
) { }
