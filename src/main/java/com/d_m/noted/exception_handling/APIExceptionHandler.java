package com.d_m.noted.exception_handling;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {
    //400
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(
            final ConstraintViolationException ex,
            final ServletWebRequest request
    ) {
        logger.info(HttpStatus.BAD_REQUEST.toString(), ex);
        final ProblemDetail data = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        data.setType(URI.create("https://developer.mozilla.org/docs/Web/HTTP/Status/400"));
        data.setDetail(ex.getMessage());
        data.setInstance( URI.create(request.getRequest().getRequestURI()));

        return handleExceptionInternal(ex, data, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.info(HttpStatus.BAD_REQUEST.toString(), ex);
        String message = "";
        if (ex.hasFieldErrors()) {
            final FieldError fieldError = ex.getFieldError();
            message = fieldError.getField() + " " + fieldError.getDefaultMessage();
        } else {
            message = ex.getMessage();
        }

        final ProblemDetail data = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        data.setType(URI.create("https://developer.mozilla.org/docs/Web/HTTP/Status/400"));
        data.setDetail(message);
        data.setInstance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()));

        return handleExceptionInternal(ex, data, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // 403
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleForbidden(final RuntimeException ex, final ServletWebRequest request) {
        logger.info(HttpStatus.FORBIDDEN.toString(), ex);
        final ProblemDetail data = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        data.setType(URI.create("https://developer.mozilla.org/docs/Web/HTTP/Status/403"));
        data.setDetail(ex.getMessage());
        data.setInstance(URI.create(request.getRequest().getRequestURI()));

        return handleExceptionInternal(ex, data, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    // 404
    @ExceptionHandler({ EntityNotFoundException.class })
    public ResponseEntity<Object> handleNotFound(final RuntimeException ex, final ServletWebRequest request) {
        logger.info(HttpStatus.NOT_FOUND.toString(), ex);
        final ProblemDetail data = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        data.setType(URI.create("https://developer.mozilla.org/docs/Web/HTTP/Status/404"));
        data.setDetail(ex.getMessage());
        data.setInstance(URI.create(request.getRequest().getRequestURI()));

        return handleExceptionInternal(ex, data, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    //405
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        pageNotFoundLogger.warn(ex.getMessage());
        final ProblemDetail data = ProblemDetail.forStatus(HttpStatus.METHOD_NOT_ALLOWED);
        data.setType(URI.create("https://developer.mozilla.org/docs/Web/HTTP/Status/405"));
        data.setDetail(ex.getMessage());
        data.setInstance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()));

        return this.handleExceptionInternal(ex, data, headers, HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    // 409
    @ExceptionHandler({ InvalidDataAccessApiUsageException.class, DataAccessException.class, EntityExistsException.class })
    public ResponseEntity<Object> handleConflict(final RuntimeException ex, final ServletWebRequest request) {
        logger.info(HttpStatus.CONFLICT.toString(), ex);
        final ProblemDetail data = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        data.setType(URI.create("https://developer.mozilla.org/docs/Web/HTTP/Status/409"));
        data.setDetail(ex.getMessage());
        data.setInstance(URI.create(request.getRequest().getRequestURI()));

        return handleExceptionInternal(ex, data, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }


    // 500
    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final ServletWebRequest request) {
        logger.error(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex);
        final ProblemDetail data = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        data.setType(URI.create("https://developer.mozilla.org/docs/Web/HTTP/Status/500"));
        data.setDetail("Encountered an internal error. Please, try again later.");
        data.setInstance(URI.create(request.getRequest().getRequestURI()));

        return handleExceptionInternal(ex, data, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
