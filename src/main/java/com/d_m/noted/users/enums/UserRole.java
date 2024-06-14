package com.d_m.noted.users.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN");

    private final String stringifiedValue;

    private UserRole(String value) {
        stringifiedValue = value;
    }
}
