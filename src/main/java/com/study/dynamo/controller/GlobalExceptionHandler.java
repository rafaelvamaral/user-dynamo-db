package com.study.dynamo.controller;

import com.study.dynamo.exception.BaseException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(ConstraintViolationException.class)
//    public Map<String, Object> handleValidationExceptions(ConstraintViolationException ex) {
//        var errors = ex.getConstraintViolations().stream()
//                .map(ConstraintViolation::getMessageTemplate)
//                .collect(Collectors.toSet());
//        return buildErrorBody(errors);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
//        var errors = ex.getBindingResult().getAllErrors().stream()
//                .map(ObjectError::getDefaultMessage)
//                .collect(Collectors.toSet());
//        var body = buildErrorBody(errors);
//        return ResponseEntity.status(status).body(body);
//    }
//
//    private static HashMap<String, Object> buildErrorBody(Set<String> errors) {
//        var body = new HashMap<String, Object>();
//        body.put("error_title", "Parâmetros incorretos na requisição");
//        body.put("errors", errors);
//        return body;
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, Object> handleValidationExceptions(ConstraintViolationException ex) {
        return buildErrorBody(ex.getConstraintViolations(), ConstraintViolation::getMessage);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        var body = buildErrorBody(ex.getBindingResult().getAllErrors(), ObjectError::getDefaultMessage);
        return ResponseEntity.status(status).body(body);
    }

    private static <T> HashMap<String, Object> buildErrorBody(Collection<T> errors, Function<T, String> messageMapper) {
        var body = new HashMap<String, Object>();
        var errorMessages = errors.stream().map(messageMapper);
        body.put("error_title", "Parâmetros incorretos na requisição");
        body.put("errors", errorMessages);
        return body;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleBaseException(RuntimeException runtimeException) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(Map.of("error_title", "Ocorreu um erro inesperado",
                        "error_description", runtimeException.getMessage()));
    }


    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleBaseException(BaseException baseException) {
        return ResponseEntity
                .status(baseException.getHttpStatus())
                .body(Map.of("error_title", baseException.getErrorTitle(),
                        "error_description", baseException.getMessage()));
    }

}
