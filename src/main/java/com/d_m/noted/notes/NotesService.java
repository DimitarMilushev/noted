package com.d_m.noted.notes;

import com.d_m.noted.notebooks.NotebooksRepository;
import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.notes.entities.Note;
import com.d_m.noted.shared.dtos.notes.CreateNoteDto;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotesService {
    private final NotesRepository repository;
    private final NotebooksRepository notebooksRepository;

    @Autowired
    public NotesService(
            NotesRepository repository,
            NotebooksRepository notebooksRepository
    ) {
        this.repository = repository;
        this.notebooksRepository = notebooksRepository;
    }

    public Note getById(Long id) {
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Failed to find note with id " + id)
                );
    }

    public Note createNote(CreateNoteDto payload) {
        if (this.repository.findByTitleAndNotebookId(payload.title(), payload.notebookId()).isPresent()) {
            throw new EntityExistsException("Note " + payload.title() + " already exists in notebook " + payload.notebookId());
        }
        final Notebook notebook = this.notebooksRepository
                .findById(payload.notebookId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Failed to find notebook with id " + payload.notebookId())
                );

        final Note note = Note.builder()
                .title(payload.title())
                .notebook(notebook)
                .content(payload.content())
                .build();

        return this.repository.save(note);
    }
}
