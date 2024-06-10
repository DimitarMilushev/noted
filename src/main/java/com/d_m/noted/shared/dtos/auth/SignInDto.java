package com.d_m.noted.shared.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record SignInDto(
        @Email
        String email,
        @NotEmpty
        String password
) {
}
