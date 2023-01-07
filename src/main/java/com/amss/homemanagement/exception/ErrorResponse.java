package com.amss.homemanagement.exception;

import java.util.List;

public record ErrorResponse(List<ErrorEntity> errorMessages) {
}
