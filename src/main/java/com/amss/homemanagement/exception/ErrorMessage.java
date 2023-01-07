package com.amss.homemanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    INTERNAL_SERVER_ERROR(1, "Internal server error. Oops, something went wrong!"),
    ALREADY_EXISTS(2, "{0} already exists!"),
    NOT_FOUND(3, "The {0} with id={1} was not found!"),
    FORBIDDEN(4, "This action is forbidden!"),
    ALREADY_PART_OF_FAMILY(5, "You are already part of the family!"),
    MUST_BE_ADMIN_FAMILY_MEMBER(6, "You must be a family member and have admin rights to perform this action!");

    private final int errorCode;
    private final String errorMessage;
}
