package com.candyshop.controller.advice;

import com.candyshop.exception.ImageUploadException;
import com.candyshop.exception.AccessDeniedException;
import com.candyshop.exception.ExceptionBody;
import com.candyshop.exception.ResourceMappingException;
import com.candyshop.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handleResourceNotFound(
            final ResourceNotFoundException e) {
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleAuthentication(
            final AuthenticationException e) {
        return new ExceptionBody("Authentication failed.");
    }

    @ExceptionHandler(ResourceMappingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handleResourceMapping(
            final ResourceMappingException e) {
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleIllegalState(final IllegalStateException e) {
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class,
            org.springframework.security.access.AccessDeniedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleAccessDenied() {
        return new ExceptionBody("Access. denied");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleArgumentNotValid(final MethodArgumentNotValidException e) {
        ExceptionBody eBody = new ExceptionBody("Validation failed");
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        eBody.setErrors(fieldErrors.stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage)));
        return eBody;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleConstraintViolation(final ConstraintViolationException e) {
        ExceptionBody eBody = new ExceptionBody("Validation failed");
        eBody.setErrors(e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage()
                )));
        return eBody;
    }

    @ExceptionHandler(ImageUploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleImageUpload(final ImageUploadException e) {
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handleOtherExceptions(final Exception e) {
        e.printStackTrace();
        return new ExceptionBody(
                "Try later. Problems on the server : " + e.getMessage());
    }
}
