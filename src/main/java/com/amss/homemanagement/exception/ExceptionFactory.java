package com.amss.homemanagement.exception;

import com.amss.homemanagement.exception.model.*;
import org.springframework.http.HttpStatus;

public class ExceptionFactory {
    public AbstractApiException createException(HttpStatus httpStatus, ErrorMessage errorMessage, Object... params) {
        return switch (httpStatus) {
            case BAD_REQUEST -> new BadRequestException(errorMessage, params);
            case NOT_FOUND -> new NotFoundException(errorMessage, params);
            case CONFLICT -> new ConflictException(errorMessage, params);
            case FORBIDDEN -> new ForbiddenException(errorMessage, params);
            default -> new InternalServerErrorException(errorMessage, params);
        };
    }
}
