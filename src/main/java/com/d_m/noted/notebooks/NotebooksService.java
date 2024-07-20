package com.d_m.noted.notebooks;

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

    public Notebook createNotebook(String title, Long userId) {
        if (repository.findByTitleAndUserId(title, userId).isPresent()) {
            throw new EntityExistsException("Notebook with title " + title + " already exists");
        }
        final UserData user = this.usersService.findById(userId);
        final Notebook notebook = Notebook.builder()
                .title(title)
                .user(user)
                .build();

        return this.repository.save(notebook);
    }

    public Notebook getById(Long id, Long userId) {
        return this.repository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Failed to find notebook with id " + id
                )
        );
    }

    Iterable<Notebook> findAllByUserId(Long userId) {
        return this.repository.findAllByUserId(userId);
    }

    public void deleteById(Long id, Long userId) {
        final Notebook notebook = this.repository.findById(id).orElseThrow(() ->
                 new EntityNotFoundException(
                        "Failed to find notebook with id " + id
                ));
        checkResourceAccessByUserId(notebook, userId);

        this.repository.delete(notebook);
    }

    public Notebook updateTitle(Long id, String title, Long userId) {
        final Notebook notebook = this.repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        "Failed to find notebook with id " + id
                ));
        checkResourceAccessByUserId(notebook, userId);
        notebook.setTitle(title);

        return this.repository.save(notebook);
    }

    private void checkResourceAccessByUserId(Notebook notebook, Long userId) {
        final Long ownerId = notebook.getUser().getId();

        if (!ownerId.equals(userId)) {
            throw new AccessDeniedException("User " + userId + " doesn't have access to " + ownerId);
        }
    }

}
