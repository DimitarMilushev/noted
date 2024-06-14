package com.d_m.noted.notebooks;

import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.notes.NotesRepository;
import com.d_m.noted.shared.dtos.notebooks.CreateNotebookDto;
import com.d_m.noted.users.UsersRepository;
import com.d_m.noted.users.entities.UserData;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotebooksService {
    private final NotebooksRepository repository;
    private final UsersRepository usersRepository;
    private final NotesRepository notesRepository;

    @Autowired
    public NotebooksService(
            NotebooksRepository repository,
            UsersRepository usersRepository,
            NotesRepository notesRepository
            ) {
        this.repository = repository;
        this.usersRepository = usersRepository;
        this.notesRepository = notesRepository;
    }

    public Notebook getById(Long id) {
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Failed to find notebook with id " + id)
                );
    }

    public Notebook createNotebook(String title, Long userId) {
        if (repository.findByTitleAndUserId(title, userId).isPresent()) {
            throw new EntityExistsException("Notebook with title " + title + " already exists");
        }
        final UserData user = this.usersRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId.toString()));

        final Notebook notebook = Notebook.builder()
                .title(title)
                .user(user)
                .build();

        return this.repository.save(notebook);
    }

}
