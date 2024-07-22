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

@Service
@AllArgsConstructor
public class UsersService {
    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserData createUser(SignUpDto payload) {
        if (this.repository.existsByEmail(payload.email())) {
            throw new EntityExistsException("User with email " + payload.email() + " already exists");
        }

        UserData user = UserData.builder()
                .email(payload.email())
                .username(payload.username())
                .password(passwordEncoder.encode(payload.password()))
                .build();

        return this.repository.save(user);
    }

    public UserData getById(Long id, UserPrincipal user) {
        final UserData userData = this.repository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!user.isAdmin()) checkResourceAccessByUserId(userData, user.getId());

        return userData;
    }

    public void changePasswordByEmail(ChangePasswordDto payload) {
        final UserData user = this.repository
                .findByEmail(payload.email())
                .orElseThrow(
                        () -> new EntityNotFoundException("Failed to find user with email " + payload.email()
                        )
                );
        if (passwordEncoder.matches(payload.password(), user.getPassword())) {
            throw new RuntimeException("Cannot change duplicate password for " + payload.email());
        }

        user.setPassword(this.passwordEncoder.encode(payload.password()));
        this.repository.save(user);
    }

    private void checkResourceAccessByUserId(UserData userData, Long userId) {
        final Long ownerId = userData.getId();

        if (!ownerId.equals(userId)) {
            throw new AccessDeniedException("User " + userId + " doesn't have access to " + userData.getId());
        }
    }
}
