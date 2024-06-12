package com.d_m.noted.shared.dtos.auth;

import jakarta.validation.constraints.NotEmpty;

public record ChangePasswordDto(
        @NotEmpty
                //TODO: email format
        String email,
        @NotEmpty
        // TODO: @lombok.Format(...)
        String password
) {
}
