package com.amss.homemanagement.exception;

import com.amss.homemanagement.exception.model.AbstractApiException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleCustomException(AbstractApiException exception) {
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(getSingleErrorResponse(exception.getErrorCode(), exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<ErrorEntity> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(e -> new ErrorEntity(-1, capitalizeFirst(e.getField()) + " " + e.getDefaultMessage()))
                .toList();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(getMultipleErrorsResponse(errors));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException exception) {
        List<ErrorEntity> errors = exception.getConstraintViolations().stream()
                .map(e -> new ErrorEntity(-1, e.getPropertyPath() + " " + e.getMessage()))
                .toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getMultipleErrorsResponse(errors));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDefaultException(Throwable exception) {
        exception.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(getSingleErrorResponse(
                        ErrorMessage.INTERNAL_SERVER_ERROR.getErrorCode(),
                        ErrorMessage.INTERNAL_SERVER_ERROR.getErrorMessage()));
    }

    private String capitalizeFirst(String str) {
        return str.isBlank() ? "" : str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private ErrorResponse getSingleErrorResponse(int errorCode, String errorMessage) {
        return new ErrorResponse(List.of(new ErrorEntity(errorCode, errorMessage)));
    }

    private ErrorResponse getMultipleErrorsResponse(List<ErrorEntity> errors) {
        return new ErrorResponse(errors);
    }
}
