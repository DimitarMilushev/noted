package com.d_m.noted.exception_handling;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.net.URI;

public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public AuthenticationExceptionHandler() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        final ProblemDetail data = ProblemDetail.forStatus(HttpServletResponse.SC_UNAUTHORIZED);
        data.setType(URI.create("https://developer.mozilla.org/docs/Web/HTTP/Status/401"));
        data.setDetail("You are not authorized to perform this action.");
        data.setInstance( URI.create(request.getRequestURI()));

        response.getOutputStream().println(objectMapper.writeValueAsString(data));
    }
}
