package com.amss.homemanagement.exception.model;

import com.amss.homemanagement.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public class ConflictException extends AbstractApiException {
    public ConflictException(ErrorMessage errorMessage, Object... params) {
        super(HttpStatus.CONFLICT, errorMessage, params);
    }
}
