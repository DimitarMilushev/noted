package com.d_m.noted.shared.dtos.auth;

import com.d_m.noted.users.enums.UserRole;

public record SignInResponseDto(
        String email,
        String username,
        UserRole role
) {}
