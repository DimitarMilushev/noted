package com.d_m.noted.auth.models;

import com.d_m.noted.users.entities.UserData;
import com.d_m.noted.users.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@Getter
public class UserPrincipal implements UserDetails {
    private final Long id;
    private final String email;
    private final String username;
    private final String password;
    private final UserRole role;

    public static UserPrincipal fromUserData(UserData data) {
        return UserPrincipal.builder()
                .id(data.getId())
                .email(data.getEmail())
                .username(data.getUsername())
                .password(data.getPassword())
                .role(data.getRole())
                .build();
    }

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.roleWithPrefix()));
    }
    private String roleWithPrefix() {
        return "ROLE_" + role.getStringifiedValue();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
