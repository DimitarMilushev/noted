package com.d_m.noted.users;

import com.d_m.noted.helpers.PasswordHelper;
import com.d_m.noted.notebooks.NotebooksService;
import com.d_m.noted.shared.dtos.auth.SignInDto;
import com.d_m.noted.shared.dtos.auth.SignUpDto;
import com.d_m.noted.users.entities.UserData;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UsersService {
    private final UsersRepository repository;
    private final PasswordHelper passwordHelper;
    @Autowired
    public UsersService(
            UsersRepository repository,
            PasswordHelper passwordHelper
    ) {
        this.repository = repository;
        this.passwordHelper = passwordHelper;
    }

    public UserData findByEmailAndPassword(SignInDto payload) {
        final String hashedPassword = passwordHelper.hashPassword(passwordHelper.hashPassword(payload.password()));
        return this.repository.findByEmailAndPassword(
                payload.email(),
                hashedPassword
        ).orElseThrow(() -> new EntityNotFoundException("Failed to find user with email and password combination"));
    }
    public UserData createUser(SignUpDto payload) {
        if (this.repository.existsByEmail(payload.email())) {
            throw new EntityExistsException("User with email " + payload.email() + " already exists");
        }

        final String hashedPassword = passwordHelper.hashPassword(payload.password());
        UserData user = UserData.builder()
                .email(payload.email())
                .username(payload.username())
                .password(hashedPassword).build();

        return this.repository.save(user);
    }

    public UserData findById(Long id) {
        return this.repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
