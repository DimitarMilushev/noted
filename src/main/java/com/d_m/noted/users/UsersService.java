package com.d_m.noted.users;

import com.d_m.noted.auth.models.UserPrincipal;
import com.d_m.noted.shared.dtos.auth.ChangePasswordDto;
import com.d_m.noted.shared.dtos.auth.SignUpDto;
import com.d_m.noted.users.entities.UserData;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UsersService {
    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserData createUser(SignUpDto payload) {
        if (this.repository.existsByEmail(payload.email())) {
            throw new EntityExistsException("User with email " + payload.email() + " already exists");
        }

        final UserData user = UserData.builder()
                .email(payload.email())
                .username(payload.username())
                .password(passwordEncoder.encode(payload.password()))
                .build();

        return this.repository.save(user);
    }

    public UserData getById(Long id, UserPrincipal user) {
        if (!user.isAdmin() && !user.getId().equals(id)) {
            throw new AccessDeniedException("User " + user.getId() + " doesn't have access to " + id);
        }

        return this.repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Failed to find user data with id " + id));
    }
//
//    public void changePasswordByEmail(ChangePasswordDto payload) {
//        final UserData data = this.repository
//                .findByEmail(payload.email())
//                .orElseThrow(
//                        () -> new EntityNotFoundException("Failed to find user with email " + payload.email()
//                        )
//                );
//        if (passwordEncoder.matches(payload.password(), data.getPassword())) {
//            throw new RuntimeException("Cannot change duplicate password for " + payload.email());
//        }
//
//        data.setPassword(this.passwordEncoder.encode(payload.password()));
//        this.repository.save(data);
//    }
}
