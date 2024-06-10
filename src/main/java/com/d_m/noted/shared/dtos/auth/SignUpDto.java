package com.d_m.noted.shared.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record SignUpDto(
        @Email
        String email,
        @NotEmpty
        String username,
        @NotEmpty
        String password //TODO: add constraints
) {}
