package com.d_m.noted.notes;

import com.d_m.noted.auth.models.UserPrincipal;
import com.d_m.noted.notes.entities.Note;
import com.d_m.noted.shared.dtos.notes.CreateNoteDto;
import com.d_m.noted.shared.dtos.notes.GetNoteDataResponseDto;
import com.d_m.noted.shared.dtos.notes.UpdateNoteContentDto;
import com.d_m.noted.shared.dtos.notes.UpdateNoteSharedStatusDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
            @AuthenticationPrincipal UserPrincipal user
    ) {
        this.service.deleteById(id, user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/shared-status")
    public ResponseEntity<Void> updateSharedStatus(
            @RequestBody UpdateNoteSharedStatusDto payload,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        this.service.changeStatusById(payload.noteId(), payload.isShared(), user);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<String> createNote(
            @RequestBody CreateNoteDto payload,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        final Note note = this.service.createNote(payload, user);
        return ResponseEntity.ok("Created note " + note.getTitle());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetNoteDataResponseDto> getNoteData(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        final Note note = this.service.getById(id, user);
        final GetNoteDataResponseDto response = this.mapper.noteToGetNoteDataResponseDto(note);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GetNoteDataResponseDto> updateNoteContent(
            @PathVariable Long id,
            @RequestBody UpdateNoteContentDto payload,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        final Note updated = this.service.updateContentById(id, payload, user);
        final GetNoteDataResponseDto response = this.mapper.noteToGetNoteDataResponseDto(updated);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/last-updated")
    public ResponseEntity<Iterable<GetNoteDataResponseDto>> last5UpdatedNotes(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        final List<Note> notes = this.service.getLast5UpdatedNotesByUserId(user.getId());
        final List<GetNoteDataResponseDto> response = notes
                .stream()
                .map(this.mapper::noteToGetNoteDataResponseDto)
                .toList();

        return ResponseEntity.ok(response);
    }
}
