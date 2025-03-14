package com.mymarket.web.error;

import com.mymarket.web.ApplicationLocaleHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@AllArgsConstructor
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ApplicationException.class})
    protected ResponseEntity<Object> handleApplicationException(ApplicationException ex, WebRequest request) {
        return getResponseEntity(ex, request, INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(value = {DataAccessException.class})
    protected ResponseEntity<Object> handleDataAccessException(DataAccessException ex, WebRequest request) {
        log.error("error access data", ex);
        return getResponseEntity(ex, request, INTERNAL_SERVER_ERROR, "data-access-error");
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
        NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return getResponseEntity(ex, request, NOT_FOUND, "not-found-error");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = getBadRequestErrors(ex);
        return handleExceptionInternal(ex, Map.of("errors", errors), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
        HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = ex.getAllErrors().stream().map(error ->
            BadRequestError.builder()
                .field("general")
                .defaultMessage(getMessage(error.getDefaultMessage()))
                .build()).toList();
        return handleExceptionInternal(ex, Map.of("errors", errors), headers, status, request);
    }

    @ExceptionHandler(value = {ApplicationBindException.class})
    protected ResponseEntity<Object> handleApplicationBindException(ApplicationBindException ex, WebRequest request) {
        var errors = getBadRequestErrors(ex);
        return handleExceptionInternal(ex, Map.of("errors", errors), new HttpHeaders(), BAD_REQUEST, request);
    }

    private List<BadRequestError> getBadRequestErrors(BindException ex) {
        return ex.getBindingResult().getFieldErrors().stream().map(error ->
            BadRequestError.builder()
                .field(getFieldName(error))
                .defaultMessage(getDefaultMessage(error))
                .build()).toList();
    }

    private String getFieldName(FieldError error) {
        return error.getField().replace(".", "-");
    }

    private String getDefaultMessage(FieldError error) {
        var defaultMessage = error.getDefaultMessage();
        assert defaultMessage != null;
        if (defaultMessage.startsWith("{")) {
            return getMessage(defaultMessage.substring(1, defaultMessage.length() - 1));
        } else {
            return defaultMessage;
        }
    }

    private ResponseEntity<Object> getResponseEntity(Exception ex, WebRequest request, HttpStatus status, String code) {
        var applicationError = ApplicationError.builder()
            .httpStatus(status)
            .statsCode(status.value())
            .message(getMessage(code)).build();
        return handleExceptionInternal(ex, applicationError, new HttpHeaders(),
            applicationError.getHttpStatus(), request);
    }

    private String getMessage(String code) {
        var locale = ApplicationLocaleHolder.getLocale();
        return getMessageSource().getMessage(code, null, locale);
    }
}
