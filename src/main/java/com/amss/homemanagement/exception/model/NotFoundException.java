package com.amss.homemanagement.exception.model;

import com.amss.homemanagement.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public class NotFoundException extends AbstractApiException {
    public NotFoundException(ErrorMessage errorMessage, Object... params) {
        super(HttpStatus.NOT_FOUND, errorMessage, params);
    }
}
