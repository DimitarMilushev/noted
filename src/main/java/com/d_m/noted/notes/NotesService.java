package com.d_m.noted.notes;

import com.d_m.noted.notebooks.NotebooksService;
import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.notes.entities.Note;
import com.d_m.noted.shared.dtos.notes.CreateNoteDto;
import com.d_m.noted.shared.dtos.notes.UpdateNoteContentDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotesService {
    private final NotesRepository repository;
    private final NotebooksService notebooksService;

    public Note getById(Long id, Long userId) {
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Failed to find note with id " + id)
                );
    }

    public Note createNote(CreateNoteDto payload, Long userId) {
        final Notebook notebook = this.notebooksService.getById(payload.notebookId(),userId);
        final Note note = Note.builder()
                .title(payload.title())
                .notebook(notebook)
                .content(payload.content())
                .build();

        return this.repository.save(note);
    }

    public void changeStatusById(Long userId, Long id, boolean isShared) {
        final Note note = this.getById(id, userId);
        checkResourceAccessByUserId(note, userId);

        note.setShared(isShared);

        this.repository.save(note);
    }

    public Note updateContentById(Long userId, Long id, UpdateNoteContentDto payload) {
        final Note note = this.getById(id, userId);
        checkResourceAccessByUserId(note, userId);

        if (payload.title() != null) {
            note.setTitle(payload.title());
        }
        if (payload.content() != null) {
            note.setContent(payload.content());
        }

        return repository.save(note);
    }

    private void checkResourceAccessByUserId(Note note, Long userId) {
        final Long ownerId = note.getNotebook().getUser().getId();

        if (!ownerId.equals(userId)) {
            throw new AccessDeniedException("User " + userId + " doesn't have access to " + ownerId);
        }
    }

    public List<Note> getLast5UpdatedNotesByUserId(Long userId) {
        return repository.findTop5ByNotebook_User_IdOrderByUpdatedAtDesc(userId);
    }

    public void deleteById(Long id, Long userId) {
        final Note note = this.getById(id, userId);
        checkResourceAccessByUserId(note, userId);

        this.repository.delete(note);
    }
}
