package com.d_m.noted.notes;

import com.d_m.noted.auth.models.UserPrincipal;
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

    public Note getById(Long id, UserPrincipal user) {
        final Note note = this.repository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Failed to find note with id " + id)
                );
        if(!user.isAdmin()) checkResourceAccessByUserId(note, user.getId());

        return note;
    }

    public Note createNote(CreateNoteDto payload, UserPrincipal user) {
        final Notebook notebook = this.notebooksService.getById(payload.notebookId(), user);
        final Note note = Note.builder()
                .title(payload.title())
                .notebook(notebook)
                .content(payload.content())
                .build();

        return this.repository.save(note);
    }

    public void changeStatusById(Long id, boolean isShared, UserPrincipal user) {
        final Note note = this.getById(id, user);
        note.setShared(isShared);

        this.repository.save(note);
    }

    public Note updateContentById(Long id, UpdateNoteContentDto payload, UserPrincipal user) {
        final Note note = this.getById(id, user);
        if (payload.title() != null) note.setTitle(payload.title());
        if (payload.content() != null) note.setContent(payload.content());

        return repository.save(note);
    }

    private void checkResourceAccessByUserId(Note note, Long userId) {
        final Long ownerId = note.getNotebook().getUser().getId();

        if (!ownerId.equals(userId)) {
            throw new AccessDeniedException("User " + userId + " doesn't have access to " + note.getId());
        }
    }

    public List<Note> getLast5UpdatedNotesByUserId(Long userId) {
        return repository.findTop5ByNotebook_User_IdOrderByUpdatedAtDesc(userId);
    }

    public void deleteById(Long id, UserPrincipal user) {
        final Note note = this.getById(id, user);
        this.repository.delete(note);
    }
}
