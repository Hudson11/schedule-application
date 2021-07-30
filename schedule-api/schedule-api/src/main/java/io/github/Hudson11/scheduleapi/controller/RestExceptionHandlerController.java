package io.github.Hudson11.scheduleapi.controller;

import io.github.Hudson11.scheduleapi.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandlerController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponse> responseExceptionStatusHandler (ResponseStatusException ex) {
        List<String> errors = new ArrayList<String>();
        errors.add(ex.getReason());
        errors.add(ex.getMessage());
        return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> constraintViolationException (ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream().map((cv) -> {
            return cv == null ? "null" : cv.getPropertyPath() + ": " + cv.getMessage();
        }).collect(Collectors.toList());
        return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(errors), HttpStatus.BAD_REQUEST);
    }
}
