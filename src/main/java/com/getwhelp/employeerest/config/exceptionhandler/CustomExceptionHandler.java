package com.getwhelp.employeerest.config.exceptionhandler;

import com.getwhelp.employeerest.dto.response.ErrorResponse;
import com.getwhelp.employeerest.dto.response.FieldErrorResponse;
import com.getwhelp.employeerest.exception.RecordNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRecordNotFoundException(final RecordNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(NOT_FOUND.value(), exception.getMessage()), NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(final Exception exception) {
        return new ResponseEntity<>(new ErrorResponse(NOT_FOUND.value(), "Unhandled Exception!"), BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final FieldErrorResponse fieldErrorResponse = new FieldErrorResponse(BAD_REQUEST.value(), "Validation errors");
        ex.getBindingResult().getFieldErrors().stream().forEach(err -> fieldErrorResponse.addFieldError(err.getDefaultMessage()));
        return handleExceptionInternal(ex, fieldErrorResponse, headers, BAD_REQUEST, request);
    }
}
