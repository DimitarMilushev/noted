package com.d_m.noted.notebooks;

import com.d_m.noted.auth.models.UserPrincipal;
import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.users.UsersService;
import com.d_m.noted.users.entities.UserData;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotebooksService {
    private final NotebooksRepository repository;
    private final UsersService usersService;

    public Notebook createNotebook(String title, UserPrincipal user) {
        if (repository.findByTitleAndUserId(title, user.getId()).isPresent()) {
            throw new EntityExistsException("Notebook with title " + title + " already exists");
        }
        final UserData userData = this.usersService.getById(user.getId(), user);
        final Notebook notebook = Notebook.builder()
                .title(title)
                .user(userData)
                .build();

        return this.repository.save(notebook);
    }

    public Notebook getById(Long id, UserPrincipal user) {
        final Notebook notebook = this.repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Failed to find notebook with id " + id
                )
        );
        if (!user.isAdmin()) checkResourceAccessByUserId(notebook, user.getId());

        return notebook;
    }

    Iterable<Notebook> findAllByUserId(Long userId) {
        return this.repository.findAllByUserId(userId);
    }

    public void deleteById(Long id, UserPrincipal user) {
        final Notebook notebook = this.getById(id, user);
        this.repository.delete(notebook);
    }

    public Notebook updateTitle(Long id, String title, UserPrincipal user) {
        final Notebook notebook = this.getById(id, user);
        notebook.setTitle(title);

        return this.repository.save(notebook);
    }

    private void checkResourceAccessByUserId(Notebook notebook, Long userId) {
        final Long ownerId = notebook.getUser().getId();

        if (!ownerId.equals(userId)) {
            throw new AccessDeniedException("User " + userId + " doesn't have access to " + notebook.getId());
        }
    }
}
