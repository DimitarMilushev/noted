package com.d_m.noted.notes;

import com.d_m.noted.notes.entities.Note;
import com.d_m.noted.auth.models.UserSessionDetails;
import com.d_m.noted.shared.dtos.notes.CreateNoteDto;
import com.d_m.noted.shared.dtos.notes.GetNoteDataResponseDto;
import com.d_m.noted.shared.dtos.notes.UpdateNoteContentDto;
import com.d_m.noted.shared.dtos.notes.UpdateNoteSharedStatusDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
@AllArgsConstructor
public class NotesController {
    private final NotesService service;
    private final NotesMapper mapper;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoteById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserSessionDetails user
    ) {
        this.service.deleteById(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/shared-status")
    public ResponseEntity<Void> updateSharedStatus(
            @RequestBody UpdateNoteSharedStatusDto payload,
            @AuthenticationPrincipal UserSessionDetails user
    ) {
        this.service.changeStatusById(user.getId(), payload.noteId(), payload.isShared());
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<String> createNote(
            @RequestBody CreateNoteDto payload,
            @AuthenticationPrincipal UserSessionDetails user
    ) {
        final Note note = this.service.createNote(payload, user.getId());
        return ResponseEntity.ok("Created note " + note.getTitle());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetNoteDataResponseDto> getNoteData(
            @PathVariable Long id,
            @AuthenticationPrincipal UserSessionDetails user
    ) {
        final Note note = this.service.getById(id, user.getId());
        final GetNoteDataResponseDto response = mapper.noteToGetNoteDataResponseDto(note);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GetNoteDataResponseDto> updateNoteContent(
            @PathVariable Long id,
            @RequestBody UpdateNoteContentDto payload,
            @AuthenticationPrincipal UserSessionDetails user
    ) {
        final Note updated = this.service.updateContentById(user.getId(), id, payload);
        final GetNoteDataResponseDto response = mapper.noteToGetNoteDataResponseDto(updated);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/last-updated")
    public ResponseEntity<Iterable<GetNoteDataResponseDto>> last5UpdatedNotes(
            @AuthenticationPrincipal UserSessionDetails user
    ) {
        final List<Note> notes = this.service.getLast5UpdatedNotesByUserId(user.getId());
        final Iterable<GetNoteDataResponseDto> response = notes.stream().map(mapper::noteToGetNoteDataResponseDto).toList();

        return ResponseEntity.ok(response);
    }
}
