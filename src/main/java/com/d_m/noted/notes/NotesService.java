package com.d_m.noted.notes;

import com.d_m.noted.notebooks.NotebooksRepository;
import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.notes.entities.Note;
import com.d_m.noted.shared.dtos.notes.CreateNoteDto;
import com.d_m.noted.shared.dtos.notes.UpdateNoteContentDto;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Note changeStatusById(Long userId, Long id, boolean isShared) {
        final Note note = this.getById(id);
        checkUserAccess(note, userId);

        note.setShared(isShared);

        return this.repository.save(note);
    }

    @Transactional
    public Note updateContentById(Long userId, Long id, UpdateNoteContentDto payload) {
        final Note note = this.getById(id);
        checkUserAccess(note, userId);

        if (payload.title() != null) {
            note.setTitle(payload.title());
        }
        if (payload.content() != null) {
            note.setContent(payload.content());
        }

        return repository.save(note);
    }

    private void checkUserAccess(Note note, Long userId) {
        final Long ownerId = note.getNotebook().getUser().getId();

        if (!ownerId.equals(userId)) {
            throw new AccessDeniedException("User " + userId + " doesn't have access to " + ownerId);
        }
    }

    public List<Note> getLast5UpdatedNotesByUserId(Long userId) {
        return repository.findTop5ByNotebook_User_IdOrderByUpdatedAtDesc(userId);
    }
}
