package com.amss.homemanagement.exception.model;

import com.amss.homemanagement.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends AbstractApiException {
    public ForbiddenException(ErrorMessage errorMessage, Object... params) {
        super(HttpStatus.FORBIDDEN, errorMessage, params);
    }
}
