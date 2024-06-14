package com.d_m.noted.security.models;

import com.d_m.noted.users.entities.UserData;
import com.d_m.noted.users.enums.UserRole;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
public class SecurityAuthDetails implements UserDetails {
    private final String username;
    private final String password;
    private final UserRole role;

    public static SecurityAuthDetails fromUserData(UserData data) {
        return SecurityAuthDetails.builder()
                .username(data.getEmail())
                .password(data.getPassword())
                .role(data.getRole())
                .build();
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
