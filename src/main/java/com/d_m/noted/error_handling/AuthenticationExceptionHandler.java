package com.d_m.noted.error_handling;

import com.d_m.noted.shared.dtos.web.APIExceptionResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.net.URI;

public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final APIExceptionResponseDto data = APIExceptionResponseDto.builder()
                .type(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/401"))
                .title("Unauthorized")
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .detail("You are not authorized to perform this action.")
                .instance(URI.create(request.getRequestURI()))
                .build();


        response.getOutputStream().println(objectMapper.writeValueAsString(data));
    }
}
