package com.d_m.noted.notes;

import com.d_m.noted.notes.entities.Note;
import com.d_m.noted.security.models.SecurityAuthDetails;
import com.d_m.noted.shared.dtos.notes.CreateNoteDto;
import com.d_m.noted.shared.dtos.notes.GetNoteDataResponseDto;
import com.d_m.noted.shared.dtos.notes.UpdateNoteSharedStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {
    private final NotesService service;
    private final NotesMapper mapper;

    @Autowired
    public NotesController(NotesService service, NotesMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PatchMapping("/shared-status")
    public ResponseEntity<Void> updateSharedStatus(
            @RequestBody UpdateNoteSharedStatusDto payload,
            @AuthenticationPrincipal SecurityAuthDetails user
            ) {
        if (!this.service.isOwnerByUserId(payload.noteId(), user.getId())) {
            throw new AccessDeniedException("User " + user.getId() + " doesn't own " + payload.noteId());
        }
        this.service.changeStatusById(payload.noteId(), payload.isShared());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createNote(@RequestBody CreateNoteDto payload) {
        final Note note = this.service.createNote(payload);
        return ResponseEntity.ok("Created note " + note.getTitle());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetNoteDataResponseDto> getNoteData(
            @PathVariable Long id,
            @AuthenticationPrincipal SecurityAuthDetails user
    ) {
        final Note note = this.service.getById(id);
        if (!note.isShared() && !note.getNotebook().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User not authorized");
        }

        final GetNoteDataResponseDto response = mapper.noteToGetNoteDataResponseDto(note);

        return ResponseEntity.ok(response);
    }
}
