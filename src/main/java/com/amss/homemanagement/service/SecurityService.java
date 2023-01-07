package com.amss.homemanagement.service;

import com.amss.homemanagement.exception.ErrorMessage;
import com.amss.homemanagement.exception.ExceptionFactory;
import com.amss.homemanagement.filter.CustomAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.amss.homemanagement.util.SecurityConstants.USER_ID;

@Service
public class SecurityService {

    public void authorize(UUID userId) {
        if (!hasRequiredId(userId)) {
            throw new ExceptionFactory().createException(HttpStatus.FORBIDDEN, ErrorMessage.FORBIDDEN);
        }
    }

    public UUID getUserId() {
        return UUID.fromString(getCustomAuthentication().getClaims().get(USER_ID));
    }

    private boolean hasRequiredId(UUID userId) {
        return userId.toString().equals(getCustomAuthentication().getClaims().get(USER_ID));
    }

    private CustomAuthenticationToken getCustomAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof CustomAuthenticationToken)) {
            throw new ExceptionFactory().createException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.INTERNAL_SERVER_ERROR);
        }
        return (CustomAuthenticationToken) authentication;
    }
}
