package com.amss.homemanagement.exception.model;

import com.amss.homemanagement.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractApiException {
    public BadRequestException(ErrorMessage errorMessage, Object... params) {
        super(HttpStatus.BAD_REQUEST, errorMessage, params);
    }
}
