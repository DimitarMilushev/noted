package com.d_m.noted.shared.dtos.web;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.net.URI;

/**
 * A generalized error body following the RFC 7807 standard.
 * This schema is composed of five parts:
 *   @param type a URI identifier that categorizes the error
 *   @param title a brief, human-readable message about the error
 *   @param status the HTTP response code
 *   @param detail a human-readable explanation of the error
 *   @param instance a URI that identifies the specific occurrence of the error
 */
@Builder
public record APIExceptionResponseDto(
        URI type,
        String title,
        int status,
        String detail,
        URI instance
) implements Serializable {
    public static APIExceptionResponseDto fromHttpStatus(URI type, String detail, URI instance, HttpStatus httpStatus) {
        return APIExceptionResponseDto.builder()
                .type(type)
                .title(httpStatus.getReasonPhrase())
                .status(httpStatus.value())
                .detail(detail)
                .instance(instance)
                .build();
    }
}
