package com.amss.homemanagement.exception.model;

import com.amss.homemanagement.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends AbstractApiException {
    public InternalServerErrorException(ErrorMessage errorMessage, Object... params) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, params);
    }
}
