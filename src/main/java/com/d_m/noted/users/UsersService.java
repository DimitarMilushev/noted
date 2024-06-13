package com.d_m.noted.users;

import com.d_m.noted.shared.dtos.auth.ChangePasswordDto;
import com.d_m.noted.shared.dtos.auth.SignInDto;
import com.d_m.noted.shared.dtos.auth.SignUpDto;
import com.d_m.noted.users.entities.UserData;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UsersService {
    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(
            UsersRepository repository,
            @Lazy PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserData findByEmailAndPassword(SignInDto payload) {
        final UserData user = this.repository
                .findByEmail(payload.email())
                .orElseThrow(
                        () -> new EntityNotFoundException("Failed to find user with email " +
                                "and password combination")
                );
        if (!passwordEncoder.matches(payload.password(), user.getPassword())) {
            throw new EntityNotFoundException("Failed to find user with email and password combination");
        }
        return user;
    }

    public UserData createUser(SignUpDto payload) {
        if (this.repository.existsByEmail(payload.email())) {
            throw new EntityExistsException("User with email " + payload.email() + " already exists");
        }

        UserData user = UserData.builder()
                .email(payload.email())
                .username(payload.username())
                .password(passwordEncoder.encode(payload.password())).build();

        return this.repository.save(user);
    }

    public UserData findById(Long id) {
        return this.repository.findById(id).orElseThrow(EntityNotFoundException::new);
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
}
