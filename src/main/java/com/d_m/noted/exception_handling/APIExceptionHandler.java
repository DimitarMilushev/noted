package com.d_m.noted.exception_handling;

import com.d_m.noted.shared.dtos.web.APIExceptionResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
    public ResponseEntity<Object> handleBadRequest(final ConstraintViolationException ex, final ServletWebRequest request) {
        logger.info(HttpStatus.BAD_REQUEST.toString(), ex);
        final APIExceptionResponseDto data = APIExceptionResponseDto.fromHttpStatus(
                URI.create("https://developer.mozilla.org/docs/Web/HTTP/Status/400"),
                ex.getMessage(),
                URI.create(request.getRequest().getRequestURI()),
                HttpStatus.BAD_REQUEST
        );
        return handleExceptionInternal(ex, data, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // 403
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleForbidden(final RuntimeException ex, final ServletWebRequest request) {
        logger.info(HttpStatus.FORBIDDEN.toString(), ex);
        final APIExceptionResponseDto data = APIExceptionResponseDto.fromHttpStatus(
                URI.create("https://developer.mozilla.org/docs/Web/HTTP/Status/403"),
                ex.getMessage(),
                URI.create(request.getRequest().getRequestURI()),
                HttpStatus.FORBIDDEN
        );
        return handleExceptionInternal(ex, data, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    // 404
    @ExceptionHandler({ EntityNotFoundException.class })
    public ResponseEntity<Object> handleNotFound(final RuntimeException ex, final ServletWebRequest request) {
        logger.info(HttpStatus.NOT_FOUND.toString(), ex);
        final APIExceptionResponseDto data = APIExceptionResponseDto.fromHttpStatus(
                URI.create("https://developer.mozilla.org/docs/Web/HTTP/Status/404"),
                ex.getMessage(),
                URI.create(request.getRequest().getRequestURI()),
                HttpStatus.NOT_FOUND
        );
        return handleExceptionInternal(ex, data, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    //405
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        pageNotFoundLogger.warn(ex.getMessage());
        final APIExceptionResponseDto data = APIExceptionResponseDto.fromHttpStatus(
                URI.create("https://developer.mozilla.org/docs/Web/HTTP/Status/405"),
                ex.getMessage(),
                URI.create(((ServletWebRequest) request).getRequest().getRequestURI()),
                HttpStatus.METHOD_NOT_ALLOWED
        );
        return this.handleExceptionInternal(ex, data, headers, HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    // 409
    @ExceptionHandler({ InvalidDataAccessApiUsageException.class, DataAccessException.class })
    public ResponseEntity<Object> handleConflict(final RuntimeException ex, final ServletWebRequest request) {
        logger.info(HttpStatus.CONFLICT.toString(), ex);
        final APIExceptionResponseDto data = APIExceptionResponseDto.fromHttpStatus(
                URI.create("https://developer.mozilla.org/docs/Web/HTTP/Status/409"),
                ex.getMessage(),
                URI.create(request.getRequest().getRequestURI()),
                HttpStatus.CONFLICT
        );
        return handleExceptionInternal(ex, data, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }


    // 500
    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final ServletWebRequest request) {
        logger.error(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex);
        final APIExceptionResponseDto data = APIExceptionResponseDto.fromHttpStatus(
                URI.create("https://developer.mozilla.org/docs/Web/HTTP/Status/500"),
                "The API server encountered an error.",
                URI.create(request.getRequest().getRequestURI()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return handleExceptionInternal(ex, data, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
